package com.project.e_commerce.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailEvent {
    private String recipient;
    private String subject;
    private String content;
    private EmailType type;

    public enum EmailType {
        OTP, WELCOME, ORDER_CONFIRMATION, SHIPPING_UPDATE
    }
}