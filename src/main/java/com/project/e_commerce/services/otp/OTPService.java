package com.project.e_commerce.services.otp;


import com.project.e_commerce.enums.OTPType;

import com.project.e_commerce.utils.GenerateOtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.UUID;
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

    public boolean validateOtp(String email, String otp, OTPType type) {
        String redisKey = "OTP:" + email + ":" + type;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);
        
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(redisKey); // Xóa OTP sau khi sử dụng
            return true;
        }
        return false;
    }
    
    /**
     * Xác thực OTP chỉ bằng code và type, không cần email
     * @param code Mã OTP
     * @param type Loại OTP
     * @return Địa chỉ email nếu xác thực thành công, null nếu thất bại
     */
    public String verifyOTPWithoutEmail(String code, OTPType type) {
        // Lấy tất cả các key có pattern OTP:*:type
        String keyPattern = "OTP:*:" + type;
        Set<String> keys = redisTemplate.keys(keyPattern);
        
        if (keys != null) {
            for (String key : keys) {
                String storedOtp = redisTemplate.opsForValue().get(key);
                if (storedOtp != null && storedOtp.equals(code)) {
                    // Trích xuất email từ key (format: OTP:email:type)
                    String[] parts = key.split(":");
                    if (parts.length >= 2) {
                        String email = parts[1];
                        // Xóa OTP sau khi sử dụng
                        redisTemplate.delete(key);
                        return email;
                    }
                }
            }
        }
        return null;
    }
    
    public boolean verifyOTP(String email, String otp, OTPType type) {
        return validateOtp(email, otp, type);
    }
    
    public String createPasswordResetToken(String email) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
                "RESET_TOKEN:" + token,
                email,
                15, // Thời gian sống 15 phút
                TimeUnit.MINUTES
        );
        return token;
    }
    
    public String getEmailFromResetToken(String token) {
        return redisTemplate.opsForValue().get("RESET_TOKEN:" + token);
    }
    
    public void invalidateResetToken(String token) {
        redisTemplate.delete("RESET_TOKEN:" + token);
    }
}
