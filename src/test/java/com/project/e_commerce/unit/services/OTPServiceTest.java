package com.project.e_commerce.unit.services;


import com.project.e_commerce.repositories.OTPRepository;
import com.project.e_commerce.services.otp.OTPService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class OTPServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private OTPRepository otpRepository;

    @Autowired
    private OTPService otpService;

    @Test
    void testGenerateAndVerifyOTP() {
        // Test code here
    }
}
