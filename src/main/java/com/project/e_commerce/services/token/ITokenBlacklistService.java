package com.project.e_commerce.services.token;

public interface ITokenBlacklistService {
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
}
