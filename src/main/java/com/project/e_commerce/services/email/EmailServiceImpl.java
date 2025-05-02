package com.project.e_commerce.services.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


import org.springframework.stereotype.Service;


import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {
    private final JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String sourceEmail;


    public CompletableFuture<Boolean> sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sourceEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);

            javaMailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", to, e);
            return CompletableFuture.completedFuture(false);
        }
    }
}