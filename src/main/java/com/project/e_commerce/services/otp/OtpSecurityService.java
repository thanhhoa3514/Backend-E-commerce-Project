package com.project.e_commerce.services.otp;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpSecurityService {
    private final RedisTemplate<String, Integer> redisTemplate;
    private final Logger securityLogger = LoggerFactory.getLogger("SECURITY_AUDIT");
    private static final String FAILED_ATTEMPTS_PREFIX = "failed:otp:";
    private static final int MAX_FAILED_ATTEMPTS = 5;

    public boolean recordAndCheckFailedAttempt(String email) {
        String key = FAILED_ATTEMPTS_PREFIX + email;
        Integer attempts = redisTemplate.opsForValue().get(key);

        if (attempts == null) {
            redisTemplate.opsForValue().set(key, 1, 30, TimeUnit.MINUTES);
            securityLogger.warn("First failed OTP attempt for email: {}", email);
            return true;
        } else if (attempts < MAX_FAILED_ATTEMPTS) {
            redisTemplate.opsForValue().increment(key);
            securityLogger.warn("Failed OTP attempt {} for email: {}", attempts + 1, email);
            return true;
        } else {
            securityLogger.error("Too many failed OTP attempts for email: {}", email);
            return false; // Có thể block tạm thời hoặc yêu cầu xác minh thêm
        }
    }

    public void resetFailedAttempts(String email) {
        redisTemplate.delete(FAILED_ATTEMPTS_PREFIX + email);
        securityLogger.info("Reset failed attempts counter for email: {}", email);
    }
}
