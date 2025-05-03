package com.project.e_commerce.controllers;


import com.project.e_commerce.dtos.otp.OTPRequestDTO;
import com.project.e_commerce.dtos.otp.OTPVerifyRequestDTO;
import com.project.e_commerce.dtos.user.PasswordResetDTO;
import com.project.e_commerce.enums.OTPType;
import com.project.e_commerce.responses.ApiResponse;
import com.project.e_commerce.services.auth.IAuthenticationService;
import com.project.e_commerce.services.otp.OTPService;
import com.project.e_commerce.utils.MessageKeys;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/auth/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;
    private final IAuthenticationService authenticationService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse> generateOTP(@Valid @RequestBody OTPRequestDTO request) {
        otpService.generateOtp(request.getEmail(), request.getType());
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("OTP sent successfully")
                        .build()
        );
    }
    
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyOTP(@Valid @RequestBody OTPVerifyRequestDTO request) {
        // Lấy email từ OTP và type
        String email = otpService.verifyOTPWithoutEmail(request.getCode(), request.getType());
        
        if (email != null) {
            // Nếu xác thực thành công và là yêu cầu đặt lại mật khẩu, tạo token
            if (request.getType() == OTPType.PASSWORD_RESET) {
                String resetToken = otpService.createPasswordResetToken(email);
                
                Map<String, String> responseData = new HashMap<>();
                responseData.put("resetToken", resetToken);
                responseData.put("email", email);
                
                return ResponseEntity.ok(
                        ApiResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("OTP verified successfully")
                                .data(responseData)
                                .build()
                );
            }
            
            // Trả về email cho các loại OTP khác
            Map<String, String> responseData = new HashMap<>();
            responseData.put("email", email);
            
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("OTP verified successfully")
                            .data(responseData)
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("OTP verification failed")
                            .build()
            );
        }
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody PasswordResetDTO passwordResetDTO) {
        // Validate password match
        if (!passwordResetDTO.getNewPassword().equals(passwordResetDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Passwords do not match")
                            .build()
            );
        }
        
        // Get email from token
        String email = otpService.getEmailFromResetToken(passwordResetDTO.getResetToken());
        if (email == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Invalid or expired reset token")
                            .build()
            );
        }
        
        // Reset password
        boolean success = authenticationService.updatePassword(email, passwordResetDTO.getNewPassword());
        
        // Invalidate token after use
        otpService.invalidateResetToken(passwordResetDTO.getResetToken());
        
        if (success) {
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("Password reset successfully")
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Failed to reset password")
                            .build()
            );
        }
    }
}
