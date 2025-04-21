package com.project.e_commerce.services.otp;


import com.project.e_commerce.dtos.events.EmailEvent;
import com.project.e_commerce.enums.OTPType;
import com.project.e_commerce.exceptions.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpOrchestrationService {
    private final OTPService otpService;
       private final OtpRateLimitService rateLimitService;
       private final KafkaTemplate<String, EmailEvent> kafkaTemplate;
       private static final String EMAIL_TOPIC = "email-notifications";
       
       public String processOtpGeneration(String email, OTPType type) {
           // Kiểm tra rate limit
           if (!rateLimitService.allowOtpGeneration(email)) {
               throw new RateLimitExceededException("Too many OTP requests");
           }
           
           // Tạo OTP
           String otp = otpService.generateOtp(email, type);
           
           // Gửi sự kiện email qua Kafka
           sendEmailEvent(email, otp, type);
           
           return otp;
       }
       
       private void sendEmailEvent(String email, String otp, OTPType type) {
           String subject;
           String content;
           
           switch (type) {
               case REGISTRATION:
                   subject = "E-Commerce Registration: Verify Your Email";
                   content = "Your registration OTP code is: " + otp + ". Valid for 5 minutes.";
                   break;
               case PASSWORD_RESET:
                   subject = "E-Commerce: Password Reset Request";
                   content = "Your password reset OTP code is: " + otp + ". Valid for 5 minutes.";
                   break;
               case TWO_FACTOR:
                   subject = "E-Commerce: Login Verification";
                   content = "Your login verification OTP code is: " + otp + ". Valid for 5 minutes.";
                   break;
               default:
                   subject = "E-Commerce: Verification Code";
                   content = "Your verification code is: " + otp + ". Valid for 5 minutes.";
           }
           
           EmailEvent event = EmailEvent.builder()
                   .recipient(email)
                   .subject(subject)
                   .content(content)
                   .type(EmailEvent.EmailType.OTP)
                   .build();
           
           kafkaTemplate.send(EMAIL_TOPIC, email, event);
       }
}
