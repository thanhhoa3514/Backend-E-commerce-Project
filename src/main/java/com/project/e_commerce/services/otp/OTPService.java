package com.project.e_commerce.services.otp;


import com.project.e_commerce.enums.OTPType;

import com.project.e_commerce.utils.GenerateOtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class OTPService {
    private final RedisTemplate<String, String> redisTemplate;
    private final GenerateOtpUtil generateOtpUtil;


    public String generateOtp(String email, OTPType type) {
        String otp = generateOtpUtil.generateRandomCode();
        redisTemplate.opsForValue().set(
                "OTP:" + email+ ":" + type,
                otp,
                5, // thời gian sống 5 phút
                TimeUnit.MINUTES
        );
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get("OTP:" + email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete("OTP:" + email); // Xóa OTP sau khi sử dụng
            return true;
        }
        return false;
    }
    public boolean verifyOTP(String email, String otp, OTPType type) {
        return validateOtp(email, otp);
    }
}
