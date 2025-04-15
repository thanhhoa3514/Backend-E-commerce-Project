package com.project.e_commerce.services.auth.oauth2;

import com.project.e_commerce.enums.AuthProvider;
import com.project.e_commerce.exceptions.OAuth2AuthenticationProcessingException;
import com.project.e_commerce.models.Role;
import com.project.e_commerce.models.SocialAccount;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.RoleRepository;
import com.project.e_commerce.repositories.SocialAccountRepository;
import com.project.e_commerce.security.OAuth2UserInfo;
import com.project.e_commerce.security.CustomOAuth2User;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.security.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
    
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        // First check if we have a social account with this email and provider
        Optional<SocialAccount> socialAccountOptional = socialAccountRepository.findByEmailAndProvider(
                oAuth2UserInfo.getEmail(), registrationId);
        
        User user;
        if (socialAccountOptional.isPresent()) {
            // Social account exists, get the associated user and update account
            SocialAccount socialAccount = socialAccountOptional.get();
            user = socialAccount.getUser();
            updateExistingSocialAccount(socialAccount, oAuth2UserInfo);
        } else {
            // Check if user exists with this email
            Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
            
            if (userOptional.isPresent()) {
                // User exists but no social account for this provider
                user = userOptional.get();
                createSocialAccountForUser(user, oAuth2UserRequest, oAuth2UserInfo);
            } else {
                // New user, register both user and social account
                user = registerNewUserWithSocialAccount(oAuth2UserRequest, oAuth2UserInfo);
            }
        }

        return CustomOAuth2User.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUserWithSocialAccount(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        
        // Create new user
        User user = User.builder()
                .fullName(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .phoneNumber("") // This might need to be handled differently as it's required but OAuth might not provide it
                .password("") // OAuth users don't need a password
                .isActive(true)
                .build();

        // Assign default role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRole(userRole);
        
        user = userRepository.save(user);
        
        // Create associated social account
        createSocialAccountForUser(user, oAuth2UserRequest, oAuth2UserInfo);
        
        return user;
    }
    
    private void createSocialAccountForUser(User user, OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        
        SocialAccount socialAccount = SocialAccount.builder()
                .user(user)
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .provider(registrationId)
                .providerId(parseProviderId(oAuth2UserInfo.getId()))
                .build();
        
        socialAccountRepository.save(socialAccount);
    }

    private Long parseProviderId(String id) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            // Some providers might use non-numeric IDs
            // In this case, we can use a hash code or other alternative
            log.warn("Non-numeric provider ID: {}. Using hash code instead.", id);
            return (long) id.hashCode();
        }
    }

    private void updateExistingSocialAccount(SocialAccount socialAccount, OAuth2UserInfo oAuth2UserInfo) {
        socialAccount.setName(oAuth2UserInfo.getName());
        // Update other fields if necessary
        socialAccountRepository.save(socialAccount);
        
        // Also update user's name if needed
        User user = socialAccount.getUser();
        user.setFullName(oAuth2UserInfo.getName());
        userRepository.save(user);
    }
}
