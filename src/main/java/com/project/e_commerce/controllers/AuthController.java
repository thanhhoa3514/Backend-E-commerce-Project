package com.project.e_commerce.controllers;


import com.project.e_commerce.dtos.TokenRefreshRequestDTO;
import com.project.e_commerce.dtos.user.UserLoginDTO;
import com.project.e_commerce.dtos.user.UserRegisterDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.TokenRefreshException;
import com.project.e_commerce.models.RefreshToken;
import com.project.e_commerce.responses.TokenRefreshResponse;
import com.project.e_commerce.services.auth.AuthenticationService;
import com.project.e_commerce.services.jwt.JwtServiceImpl;
import com.project.e_commerce.services.refresh_tokens.IRefreshTokenService;
import com.project.e_commerce.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private final AuthenticationService authenticationService;
    private IRefreshTokenService refreshTokenService;

    private JwtServiceImpl jwtService;


    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserRegisterDTO registerDTO,
            HttpServletRequest request) {
        Map<String, String> tokens = authenticationService.register(registerDTO, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO loginDTO,
            HttpServletRequest request) {
        Map<String, String> tokens = authenticationService.login(loginDTO, request);
        return ResponseEntity.ok(tokens);
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refreshToken(
//            @Valid @RequestBody TokenRefreshDTO refreshDTO) throws DataNotFoundException {
//        Map<String, String> tokens = authenticationService.refreshToken(refreshDTO.getRefreshToken());
//        return ResponseEntity.ok(tokens);
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authenticationService.logout(token);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken() {
        // The SecurityContextHolder will only have an authentication if the token is valid
        // Spring Security's JWT filter will have already validated the token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Boolean> response = new HashMap<>();
        boolean isValid = authentication != null && authentication.isAuthenticated();
        response.put("valid", isValid);

        return ResponseEntity.ok(response);
    }
}
