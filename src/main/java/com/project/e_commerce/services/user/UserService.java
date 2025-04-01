package com.project.e_commerce.services.user;


import com.project.e_commerce.dtos.user.UserDTO;

import com.project.e_commerce.models.User;

import com.project.e_commerce.services.user.commands.IUserCommandService;
import com.project.e_commerce.services.user.queries.IUserQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserCommandService userCommandService;
    private final IUserQueryService userQueryService;

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
