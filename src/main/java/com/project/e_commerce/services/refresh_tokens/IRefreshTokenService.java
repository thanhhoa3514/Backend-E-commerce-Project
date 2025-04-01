package com.project.e_commerce.services.refresh_tokens;

import com.project.e_commerce.models.RefreshToken;
import com.project.e_commerce.models.User;

import java.util.Optional;

public interface IRefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUserId(Long userId);
}
