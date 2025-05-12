package com.project.e_commerce.controllers;

import com.project.e_commerce.components.LocalizationUtils;
import com.project.e_commerce.dtos.TokenRefreshRequestDTO;
import com.project.e_commerce.dtos.user.ResetPasswordDTO;
import com.project.e_commerce.dtos.user.UserLoginDTO;

import com.project.e_commerce.dtos.user.UserRegisterDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;

import com.project.e_commerce.models.user.User;
import com.project.e_commerce.responses.AuthResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import jakarta.servlet.http.HttpSession;

import com.project.e_commerce.responses.UserResponse;
import com.project.e_commerce.security.CustomOAuth2User;
import com.project.e_commerce.services.token.TokenBlacklistServiceImpl;
import com.project.e_commerce.services.auth.AuthenticationServiceImpl;
import com.project.e_commerce.services.jwt.IJwtService;
import com.project.e_commerce.utils.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthenticationServiceImpl authenticationServiceImpl;
        private final LocalizationUtils localizationUtils;
        private final TokenBlacklistServiceImpl tokenBlacklistServiceImpl;
        private final IJwtService jwtService;

        /**
         * Đăng nhập người dùng
         *
         * @param loginDTO thông tin đăng nhập
         * @param request  HTTP request
         * @return JWT tokens
         */
        @PostMapping("/login")
        @Operation(summary = "User login", description = "Authenticate user and return tokens")
        public ResponseEntity<AuthResponse> login(
                        @Valid @RequestBody UserLoginDTO loginDTO,
                        HttpServletRequest request) {
                Map<String, String> tokens = authenticationServiceImpl.login(loginDTO, request);
                log.info("User logged in: {}", loginDTO.getPhoneNumber());
                return ResponseEntity.ok(AuthResponse.builder()
                                .message("Đăng nhập thành công")
                                .status(HttpStatus.OK)
                                .data(tokens)
                                .build());
        }

        @PostMapping("/register")
        @Operation(summary = "User registration", description = "Register a new user without logging in")
        public ResponseEntity<AuthResponse> register(
                        @Valid @RequestBody UserRegisterDTO userRegisterDTO,
                        HttpServletRequest request) {
                User registeredUser = authenticationServiceImpl.register(userRegisterDTO, request);
                log.info("User registered: {}", userRegisterDTO.getPhoneNumber());
                return ResponseEntity.ok(
                        AuthResponse.builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                                .status(HttpStatus.CREATED)
                                .data(UserResponse.fromUser(registeredUser))
                                .build());
        }

        /**
         * Làm mới access token bằng refresh token
         *
         * @param refreshDTO refresh token
         * @return access token mới
         */
        @PostMapping("/refresh-token")
        @Operation(summary = "Refresh access token", description = "Generate a new access token using refresh token", security = {
                        @SecurityRequirement(name = "bearer-key") })
        public ResponseEntity<AuthResponse> refreshToken(
                        @Valid @RequestBody TokenRefreshRequestDTO refreshDTO) throws DataNotFoundException {
                Map<String, String> tokens = authenticationServiceImpl.refreshToken(refreshDTO.getRefreshToken());
                return ResponseEntity.ok(AuthResponse.builder()
                                .message("Token đã được làm mới")
                                .status(HttpStatus.OK)
                                .data(tokens)
                                .build());
        }

        /**
         * Đăng xuất người dùng và vô hiệu hóa token
         *
         * @param authHeader Authorization header chứa JWT token
         * @return thông báo đăng xuất thành công
         */
        @PostMapping("/logout")
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
        @Operation(summary = "User logout", description = "Invalidate the current token and log out the user", security = {
                        @SecurityRequirement(name = "bearer-key") })
        public ResponseEntity<AuthResponse> logout(
                        @RequestHeader("Authorization") String authHeader) {
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        // Thêm token vào blacklist
                        tokenBlacklistServiceImpl.blacklistToken(token);
                        // Xóa context bảo mật
                        SecurityContextHolder.clearContext();
                        log.info("User logged out successfully");
                }
                return ResponseEntity.ok(AuthResponse.builder()
                                .message("Đăng xuất thành công")
                                .status(HttpStatus.OK)
                                .data(null)
                                .build());
        }

        /**
         * Kiểm tra tính hợp lệ của token
         *
         * @return kết quả kiểm tra
         */
        @GetMapping("/validate-token")
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
        @Operation(summary = "Validate token", description = "Check if the current token is valid", security = {
                        @SecurityRequirement(name = "bearer-key") })
        public ResponseEntity<AuthResponse> validateToken() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                boolean isValid = authentication != null && authentication.isAuthenticated()
                                && !"anonymousUser".equals(authentication.getPrincipal());

                String message = isValid ? "Token hợp lệ" : "Token không hợp lệ";
                Map<String, Boolean> validationResult = Map.of("valid", isValid);

                return ResponseEntity.ok(AuthResponse.builder()
                                .message(message)
                                .status(HttpStatus.OK)
                                .data(validationResult)
                                .build());
        }

        @GetMapping("/success")
        @Operation(summary = "Get authenticated user details", description = "Return details of the authenticated OAuth2 user")
        public ResponseEntity<AuthResponse> getUser(HttpServletRequest request, Authentication authentication) {
                // Get authentication from session
                HttpSession session = request.getSession(false);

                if (session != null) {
                    SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
                    if (securityContext != null) {
                        authentication = securityContext.getAuthentication();
                        log.info("Got authentication from session: {}", authentication != null);
                    }
                }

                // If not in session, try security context
                if (authentication == null) {
                    authentication = SecurityContextHolder.getContext().getAuthentication();
                    log.info("Got authentication from SecurityContextHolder: {}", authentication != null);
                }

                // Debug information
                if (authentication == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                            AuthResponse.builder()
                                    .message("Authentication object is null")
                                    .status(HttpStatus.UNAUTHORIZED)
                                    .data(null)
                                    .build());
                }

                // Check if it's an anonymous authentication
                if (authentication instanceof AnonymousAuthenticationToken) {
                    log.warn("Received AnonymousAuthenticationToken instead of OAuth2 authentication");

                    // For testing purposes, let's create a dummy response to see if the frontend works
                    // In production, you would return an error
                    String dummyToken = "dummy_token_for_testing";
                    Map<String, Object> dummyData = Map.of(
                        "token", dummyToken,
                        "user", Map.of(
                            "id", 0,
                            "email", "test@example.com",
                            "name", "Test User"
                        ),
                        "provider", "google"
                    );

                    return ResponseEntity.ok(AuthResponse.builder()
                            .message("Authentication successful (dummy data for testing)")
                            .status(HttpStatus.OK)
                            .data(dummyData)
                            .build());

                    // Uncomment this for production
                    /*
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                            AuthResponse.builder()
                                    .message("OAuth2 authentication failed - received anonymous authentication")
                                    .status(HttpStatus.UNAUTHORIZED)
                                    .data(null)
                                    .build());
                    */
                }

                // Log authentication details
                log.info("Authentication class: {}", authentication.getClass().getName());
                log.info("Principal class: {}", authentication.getPrincipal().getClass().getName());
                log.info("Authorities: {}", authentication.getAuthorities());
                log.info("Is authenticated: {}", authentication.isAuthenticated());

                // Handle both OAuth2 and regular authentication
                String token;
                Map<String, Object> userData;

                if (authentication.getPrincipal() instanceof CustomOAuth2User customUser) {
                    token = jwtService.generateAccessToken(customUser);
                    userData = Map.of(
                        "token", token,
                        "user", customUser.getAttributes(),
                        "provider", "google"
                    );
                } else if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
                    token = jwtService.generateAccessToken(oauth2User);
                    userData = Map.of(
                        "token", token,
                        "user", oauth2User.getAttributes(),
                        "provider", "google"
                    );
                } else if (authentication.getPrincipal() instanceof User user) {
                    token = jwtService.generateAccessToken(user);
                    userData = Map.of(
                        "token", token,
                        "user", Map.of(
                            "id", user.getId(),
                            "email", user.getEmail(),
                            "fullName", user.getFullName()
                        ),
                        "provider", "local"
                    );
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        AuthResponse.builder()
                            .message("Unsupported authentication type")
                            .status(HttpStatus.UNAUTHORIZED)
                            .data(null)
                            .build());
                }

                return ResponseEntity.ok(AuthResponse.builder()
                                .message("Authentication successful")
                                .status(HttpStatus.OK)
                                .data(userData)
                                .build());
        }

        @PostMapping("/reset-password")
        @Operation(summary = "Reset user password", description = "Reset password after OTP verification")
        public ResponseEntity<AuthResponse> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
                boolean success = authenticationServiceImpl.updatePassword(
                                resetPasswordDTO.getEmail(),
                                resetPasswordDTO.getNewPassword());

                if (success) {
                        log.info("Password reset successful for email: {}", resetPasswordDTO.getEmail());
                        return ResponseEntity.ok(AuthResponse.builder()
                                        .message("Mật khẩu đã được đặt lại thành công")
                                        .status(HttpStatus.OK)
                                        .data(null)
                                        .build());
                } else {
                        log.warn("Password reset failed for email: {}", resetPasswordDTO.getEmail());
                        return ResponseEntity.badRequest().body(AuthResponse.builder()
                                        .message("Không thể đặt lại mật khẩu. Vui lòng kiểm tra lại mã OTP.")
                                        .status(HttpStatus.BAD_REQUEST)
                                        .data(null)
                                        .build());
                }
        }
}
