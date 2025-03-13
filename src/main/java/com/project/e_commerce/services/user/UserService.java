package com.project.e_commerce.services.user;

import com.project.e_commerce.components.JwtTokenUtils;
import com.project.e_commerce.dtos.UserDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Role;
import com.project.e_commerce.models.User;
import com.project.e_commerce.repositories.RoleRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.services.user.commands.IUserCommandService;
import com.project.e_commerce.services.user.queries.IUserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserCommandService userCommandService;
    private final IUserQueryService userQueryService;

    private final AuthenticationConfiguration authenticationConfiguration;

    private AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public User createUser(UserDTO userDTO) {
        return userCommandService.createUser(userDTO);
    }

    @Override
    public User getUserById(long userId) {
        return userQueryService.getUserById(userId);
    }
    /****
     * CustomUserDetailsService -> UserRepository
     * AuthenticationConfig -> CustomUserDetailsService
     * SecurityConfig -> JwtTokenFilter -> CustomUserDetailsService
     * UserService -> AuthenticationConfiguration

     * ****/

    @Override
    public String userLogin(String phoneNumber, String password) throws Exception {
        return userCommandService.login(phoneNumber, password);
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userQueryService.findByPhoneNumber(phoneNumber);
    }
}
