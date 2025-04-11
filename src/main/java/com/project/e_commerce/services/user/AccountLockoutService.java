package com.project.e_commerce.services.user;


import com.project.e_commerce.models.LoginAttempt;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.LoginAttemptRepository;
import com.project.e_commerce.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountLockoutService {
    private final LoginAttemptRepository loginAttemptRepository;
    private final UserRepository userRepository;


    @Value("${security.account.max-failed-attempts:5}")
    private int maxFailedAttempts;

    @Value("${security.account.lockout-duration-minutes:30}")
    private int lockoutDurationMinutes;


    public void checkAccountLocked(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);

        if (user != null && user.isLocked()) {
            LocalDateTime lockoutEnd = user.getLockedUntil();

            if (lockoutEnd != null && lockoutEnd.isAfter(LocalDateTime.now())) {
                log.warn("Account locked for user: {}", phoneNumber);
                throw new LockedException("Account is locked until " + lockoutEnd);
            } else if (lockoutEnd != null) {
                // Lockout period has expired, unlock the account
                unlockAccount(user);
            }
        }
    }
    @Transactional
    public void recordLoginAttempt(String phoneNumber, boolean successful, HttpServletRequest request) {
        String ipAddress = getClientIP(request);

        LoginAttempt attempt = LoginAttempt.builder()
                .phoneNumber(phoneNumber)
                .successful(successful)
                .ipAddress(ipAddress)
                .build();

        loginAttemptRepository.save(attempt);

        if (!successful) {
            checkAndLockAccount(phoneNumber);
        } else {
            // Successful login, reset any previous failed attempts by unlocking if needed
            User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
            if (user != null && user.isLocked()) {
                unlockAccount(user);
            }
        }
    }

    @Transactional
    private void checkAndLockAccount(String phoneNumber) {
        LocalDateTime checkSince = LocalDateTime.now().minusMinutes(lockoutDurationMinutes);
        int failedAttempts = loginAttemptRepository.countFailedAttempts(phoneNumber, checkSince);

        if (failedAttempts >= maxFailedAttempts) {
            User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
            if (user != null) {
                lockAccount(user);
                log.warn("Account locked for user: {} after {} failed attempts", phoneNumber, failedAttempts);
            }
        }
    }
    @Transactional
    private void lockAccount(User user) {
        user.setLocked(true);
        user.setLockedUntil(LocalDateTime.now().plusMinutes(lockoutDurationMinutes));
        userRepository.save(user);
    }

    @Transactional
    private void unlockAccount(User user) {
        user.setLocked(false);
        user.setLockedUntil(null);
        userRepository.save(user);
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
