package com.project.e_commerce.services.user.profile;


import com.project.e_commerce.dtos.user.UserProfileDTO;
import com.project.e_commerce.models.user.NotificationPreferences;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.models.user.UserProfile;
import com.project.e_commerce.repositories.UserProfileRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.services.auth.AuthenticationService;
import com.project.e_commerce.services.user.profile.mappers.IUserProfileMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements  IUserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final IUserProfileMapperService userProfileMapperService;
    private final AuthenticationService authenticationService;


    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfileByUserId(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for user ID: " + userId));
        return userProfileMapperService.mapToDTO(userProfile);
    }

    @Override
    public UserProfileDTO updateUserProfile(Long userId, UserProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for user ID: " + userId));

        userProfileMapperService.updateUserProfileFromDTO(userProfile, userProfileDTO);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapperService.mapToDTO(userProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getCurrentUserProfile() {
        User currentUser = authenticationService.getCurrentUser();
        return getUserProfileByUserId(currentUser.getId());
    }

    @Override
    public UserProfileDTO updateCurrentUserProfile(UserProfileDTO userProfileDTO) {
        User currentUser = authenticationService.getCurrentUser();
        return updateUserProfile(currentUser.getId(), userProfileDTO);
    }
    @Transactional
    public void createUserProfileForUser(User user) {
        if (userProfileRepository.findByUserId(user.getId()).isPresent()) {
            return; // Profile already exists
        }

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .build();

        NotificationPreferences notificationPreferences = NotificationPreferences.builder()
                .userProfile(userProfile)
                .emailNotifications(true)
                .pushNotifications(true)
                .smsNotifications(false)
                .marketingEmails(false)
                .build();

        userProfile.setNotificationPreferences(notificationPreferences);
        userProfileRepository.save(userProfile);
    }
}
