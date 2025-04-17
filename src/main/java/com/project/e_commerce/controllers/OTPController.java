package com.project.e_commerce.controllers;


import com.project.e_commerce.services.otp.OTPService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse> generateOTP(@Valid @RequestBody OTPRequest request) {
        otpService.generateOTP(request.getEmail(), request.getType());
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("OTP sent successfully")
                        .build()
        );
    }
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyOTP(@Valid @RequestBody OTPVerifyRequest request) {
        boolean verified = otpService.verifyOTP(request.getEmail(), request.getCode(), request.getType());
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("OTP verified successfully")
                        .build()
        );
    }
}
