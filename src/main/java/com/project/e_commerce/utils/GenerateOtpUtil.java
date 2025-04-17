package com.project.e_commerce.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class GenerateOtpUtil {
    private final Random random = new SecureRandom();
    public String generateRandomCode() {
        return String.format("%06d", random.nextInt(999999));
    }

}
