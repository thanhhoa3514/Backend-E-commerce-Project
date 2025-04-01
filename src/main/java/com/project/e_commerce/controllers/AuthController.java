package com.project.e_commerce.controllers;


import com.project.e_commerce.dtos.TokenRefreshRequestDTO;
import com.project.e_commerce.exceptions.TokenRefreshException;
import com.project.e_commerce.models.RefreshToken;
import com.project.e_commerce.responses.TokenRefreshResponse;
import com.project.e_commerce.services.jwt.JwtServiceImpl;
import com.project.e_commerce.services.refresh_tokens.IRefreshTokenService;
import com.project.e_commerce.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {


    private IRefreshTokenService refreshTokenService;

    private JwtServiceImpl jwtService;


    private UserService userService;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateAccessToken(user);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }


    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken() {
        // The SecurityContextHolder will only have an authentication if the token is valid
        // Spring Security's JWT filter will have already validated the token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Boolean> response = new HashMap<>();
        boolean isValid = authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }
}
