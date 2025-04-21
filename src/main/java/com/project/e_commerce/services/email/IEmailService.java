package com.project.e_commerce.services.email;

import java.util.concurrent.CompletableFuture;

public interface IEmailService {
    CompletableFuture<Boolean> sendEmail(String to, String subject, String content);
}

