package com.project.e_commerce.controllers;


import com.project.e_commerce.dtos.TokenRefreshRequestDTO;
import com.project.e_commerce.dtos.user.UserLoginDTO;
import com.project.e_commerce.dtos.user.UserRegisterDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.TokenRefreshException;
import com.project.e_commerce.models.RefreshToken;
import com.project.e_commerce.responses.AuthResponse;
import com.project.e_commerce.responses.TokenRefreshResponse;
import com.project.e_commerce.services.TokenBlacklistService;
import com.project.e_commerce.services.auth.AuthenticationService;
import com.project.e_commerce.services.jwt.JwtServiceImpl;
import com.project.e_commerce.services.refresh_tokens.IRefreshTokenService;
import com.project.e_commerce.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private IRefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    private JwtServiceImpl jwtService;


    private UserService userService;

    /**
     * Đăng nhập người dùng
     * @param loginDTO thông tin đăng nhập
     * @param request HTTP request
     * @return JWT tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody UserLoginDTO loginDTO,
            HttpServletRequest request) {
        Map<String, String> tokens = authenticationService.login(loginDTO, request);
        AuthResponse response = new AuthResponse(
                tokens.get("access_token"),
                tokens.get("refresh_token"),
                "Đăng nhập thành công"
        );
        log.info("User logged in: {}", loginDTO.getPhoneNumber());
        return ResponseEntity.ok(response);
    }

    /**
     * Làm mới access token bằng refresh token
     * @param refreshDTO refresh token
     * @return access token mới
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequestDTO refreshDTO) throws DataNotFoundException {
        Map<String, String> tokens = authenticationService.refreshToken(refreshDTO.getRefreshToken());
        AuthResponse response = new AuthResponse(
                tokens.get("access_token"),
                refreshDTO.getRefreshToken(),
                "Token đã được làm mới"
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Đăng xuất người dùng và vô hiệu hóa token
     * @param authHeader Authorization header chứa JWT token
     * @return thông báo đăng xuất thành công
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Thêm token vào blacklist
            tokenBlacklistService.blacklistToken(token);
            // Xóa context bảo mật
            SecurityContextHolder.clearContext();
            log.info("User logged out successfully");
        }
        return ResponseEntity.ok(new AuthResponse(null, null, "Đăng xuất thành công"));
    }

    /**
     * Kiểm tra tính hợp lệ của token
     * @return kết quả kiểm tra
     */
    @GetMapping("/validate-token")
    public ResponseEntity<AuthResponse> validateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isValid = authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        String message = isValid ? "Token hợp lệ" : "Token không hợp lệ";
        return ResponseEntity.ok(new AuthResponse(null, null, message, isValid));
    }
}
