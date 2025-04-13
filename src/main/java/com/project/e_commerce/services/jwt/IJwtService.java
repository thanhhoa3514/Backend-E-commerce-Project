package com.project.e_commerce.services.jwt;

import com.project.e_commerce.models.user.User;
import com.project.e_commerce.security.CustomOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface IJwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    String getRoleFromToken(String token);
    public boolean isTokenExpired(String token);
    String generateAccessToken(OAuth2User userPrincipal);
}
