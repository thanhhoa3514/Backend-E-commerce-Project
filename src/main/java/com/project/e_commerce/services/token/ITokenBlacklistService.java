package com.project.e_commerce.services;

public interface ITokenBlacklistService {
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
}
