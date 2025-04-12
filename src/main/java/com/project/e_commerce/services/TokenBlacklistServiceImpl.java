package com.project.e_commerce.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements ITokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    @Value("${jwt.secretKey}")
    private String jwtSecret;

    private static final String BLACKLIST_PREFIX = "token:blacklist:";

    /**
     * Thêm token vào blacklist với thời gian hết hạn tương ứng
     * @param token JWT token cần blacklist
     */
    @Override
    public void blacklistToken(String token) {
        try {
            // Lấy thời gian hết hạn từ token
            Claims claims = extractAllClaims(token);
            Date expirationDate = claims.getExpiration();

            // Tính thời gian còn lại (tính bằng giây)
            long ttl = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;

            // Nếu token đã hết hạn, không cần thêm vào blacklist
            if (ttl <= 0) {
                return;
            }

            // Thêm token vào Redis với TTL tương ứng
            String blacklistKey = BLACKLIST_PREFIX + token;
            redisTemplate.opsForValue().set(blacklistKey, "blacklisted", ttl, TimeUnit.SECONDS);
            log.info("Token added to blacklist with TTL: {} seconds", ttl);
        } catch (Exception e) {
            log.error("Error blacklisting token: {}", e.getMessage());
        }
    }
    /**
     * Kiểm tra xem token có trong blacklist không
     * @param token JWT token cần kiểm tra
     * @return true nếu token đã bị blacklist, false nếu không
     */
    @Override
    public boolean isTokenBlacklisted(String token) {
        String blacklistKey = BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(blacklistKey);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
