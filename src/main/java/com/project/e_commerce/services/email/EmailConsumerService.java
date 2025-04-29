package com.project.e_commerce.services.email;

import com.project.e_commerce.dtos.events.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConsumerService {
    private final IEmailService emailService;
    
    @KafkaListener(
        topics = "email-notifications", 
        groupId = "email-service", 
        containerFactory = "jsonKafkaListenerContainerFactory"
    )
    public void consumeEmailEvents(EmailEvent emailEvent) {
        log.info("Received email event for recipient: {}", emailEvent.getRecipient());
        
        try {
            CompletableFuture<Boolean> future = emailService.sendEmail(
                emailEvent.getRecipient(),
                emailEvent.getSubject(),
                emailEvent.getContent()
            );
            
            future.whenComplete((success, throwable) -> {
                if (throwable != null) {
                    log.error("Failed to send email to {}: {}", emailEvent.getRecipient(), throwable.getMessage());
                } else if (success) {
                    log.info("Email sent successfully to {} with type: {}", 
                        emailEvent.getRecipient(), 
                        emailEvent.getType()
                    );
                } else {
                    log.error("Failed to send email to {}", emailEvent.getRecipient());
                }
            });
        } catch (Exception e) {
            log.error("Failed to process email event: {}", e.getMessage(), e);
        }
    }
} 