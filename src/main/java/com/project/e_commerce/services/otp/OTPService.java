package com.project.e_commerce.services.otp;


import com.project.e_commerce.enums.OTPType;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.exceptions.ResourceNotFoundException;
import com.project.e_commerce.models.OTP;
import com.project.e_commerce.repositories.OTPRepository;
import com.project.e_commerce.utils.GenerateOtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final JavaMailSender mailSender;
    private final OTPRepository otpRepository;
    private final GenerateOtpUtil generateOtpUtil;
    private static final long OTP_EXPIRY_MINUTES = 5;

    @Transactional
    public OTP generateOTP(String email, OTPType type) {
        // Xóa OTP cũ nếu có
        otpRepository.deleteByEmailAndType(email, type);

        // Tạo OTP mới
        String code = generateOtpUtil.generateRandomCode();
        OTP otp = OTP.builder()
                .code(code)
                .email(email)
                .type(type)
                .expiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES))
                .verified(false)
                .build();

        // Lưu vào DB
        otp = otpRepository.save(otp);

        // Gửi email
        sendOTPEmail(email, code, type);

        return otp;
    }
    public boolean verifyOTP(String email, String code, OTPType type) throws InvalidParamException, ResourceNotFoundException {
        OTP otp = otpRepository.findByEmailAndTypeAndVerifiedFalse(email, type)
                .orElseThrow(() -> new ResourceNotFoundException("OTP not found"));

        if (otp.isExpired()) {
            throw new InvalidParamException("OTP has expired");
        }

        if (!otp.getCode().equals(code)) {
            throw new InvalidParamException("Invalid OTP");
        }

        otp.setVerified(true);
        otpRepository.save(otp);
        return true;
    }


    private void sendOTPEmail(String email, String code, OTPType type) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@yourcompany.com");
        message.setTo(email);

        switch (type) {
            case REGISTRATION:
                message.setSubject("E-Commerce Registration: Verify Your Email");
                message.setText("Your registration OTP code is: " + code + ". Valid for 5 minutes.");
                break;
            case PASSWORD_RESET:
                message.setSubject("E-Commerce: Password Reset Request");
                message.setText("Your password reset OTP code is: " + code + ". Valid for 5 minutes.");
                break;
            case TWO_FACTOR:
                message.setSubject("E-Commerce: Login Verification");
                message.setText("Your login verification OTP code is: " + code + ". Valid for 5 minutes.");
                break;
        }

        mailSender.send(message);
    }
}
