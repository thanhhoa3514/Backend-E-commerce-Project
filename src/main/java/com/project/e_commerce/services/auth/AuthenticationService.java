package com.project.e_commerce.services.auth;


import com.project.e_commerce.dtos.user.UserLoginDTO;
import com.project.e_commerce.dtos.user.UserRegisterDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.InvalidPasswordException;
import com.project.e_commerce.models.Role;
import com.project.e_commerce.models.User;
import com.project.e_commerce.repositories.RoleRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.services.jwt.IJwtService;
import com.project.e_commerce.services.product.valiadation.PasswordValidationService;
import com.project.e_commerce.services.user.AccountLockoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordValidationService passwordValidationService;
    private final AccountLockoutService accountLockoutService;
//    private final TokenBlacklistService tokenBlacklistService;


    @Transactional
    public Map<String, String> register(UserRegisterDTO registerDTO, HttpServletRequest request) {
        // Validate password strength
        List<String> passwordErrors = passwordValidationService.validatePassword(registerDTO.getPassword());
        if (!passwordErrors.isEmpty()) {
            throw new InvalidPasswordException("Password does not meet security requirements: " + String.join(", ", passwordErrors));
        }

        // Check if user already exists
        if (userRepository.existsByPhoneNumber(registerDTO.getPhoneNumber())) {
            throw new RuntimeException("Phone number already registered");
        }

        // Get default user role
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        // Create new user
        User newUser = User.builder()
                .fullName(registerDTO.getFullName())
                .phoneNumber(registerDTO.getPhoneNumber())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .address(registerDTO.getAddress())
//                .active(true)
                .role(role)
                .build();

        userRepository.save(newUser);
        log.info("User registered successfully: {}", newUser.getPhoneNumber());

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(newUser);
        String refreshToken = jwtService.generateRefreshToken(newUser);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    public Map<String, String> login(UserLoginDTO loginDTO, HttpServletRequest request) {
        try {
            // Check if account is locked before attempting authentication
            accountLockoutService.checkAccountLocked(loginDTO.getPhoneNumber());

            // Attempt authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getPhoneNumber(),
                            loginDTO.getPassword()
                    )
            );

            // If authentication successful, get user details
            User user = (User) authentication.getPrincipal();

            // Record successful login attempt
            accountLockoutService.recordLoginAttempt(loginDTO.getPhoneNumber(), true, request);
            log.info("User logged in successfully: {}", user.getPhoneNumber());

            // Generate tokens
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            return tokens;
        } catch (BadCredentialsException e) {
            // Record failed login attempt
            accountLockoutService.recordLoginAttempt(loginDTO.getPhoneNumber(), false, request);
            log.warn("Failed login attempt for user: {}", loginDTO.getPhoneNumber());
            throw new BadCredentialsException("Invalid phone number or password");
        } catch (LockedException e) {
            log.warn("Attempt to login to locked account: {}", loginDTO.getPhoneNumber());
            throw e;
        }
    }

//    public void logout(String token) {
//        if (token != null && !token.isEmpty()) {
//            // Add token to blacklist
//            tokenBlacklistService.blacklistToken(token);
//
//            // Clear security context
//            SecurityContextHolder.clearContext();
//            log.info("User logged out successfully");
//        }
//    }

    public Map<String, String> refreshToken(String refreshToken) throws DataNotFoundException {
        if (!jwtService.validateToken(refreshToken)) {
            throw new DataNotFoundException("Invalid refresh token");
        }

        String phoneNumber = jwtService.getUsernameFromToken(refreshToken);
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", newAccessToken);
        return tokens;
    }
}
