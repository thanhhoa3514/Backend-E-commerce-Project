package com.project.e_commerce.services.otp;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpRateLimitService {
    private final RedisTemplate<String, Integer> redisTemplate;
    private static final String RATE_LIMIT_PREFIX = "rate:otp:";
    private static final int MAX_ATTEMPTS = 3;
    private static final int WINDOW_MINUTES = 15;

    public boolean allowOtpGeneration(String email) {
        String key = RATE_LIMIT_PREFIX + email;
        Integer attempts = redisTemplate.opsForValue().get(key);

        if (attempts == null) {
            redisTemplate.opsForValue().set(key, 1, WINDOW_MINUTES, TimeUnit.MINUTES);
            return true;
        } else if (attempts < MAX_ATTEMPTS) {
            redisTemplate.opsForValue().increment(key);
            return true;
        }

        return false;
    }
}
