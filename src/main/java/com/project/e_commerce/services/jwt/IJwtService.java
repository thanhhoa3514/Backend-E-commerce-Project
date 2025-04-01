package com.project.e_commerce.services.jwt;

import com.project.e_commerce.models.User;

public interface IJwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    String getRoleFromToken(String token);
    public boolean isTokenExpired(String token);
}
