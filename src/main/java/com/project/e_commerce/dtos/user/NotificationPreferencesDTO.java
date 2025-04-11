package com.project.e_commerce.dtos.user;

import lombok.*;

@Data
@Builder
public class NotificationPreferencesDTO {

    private Long id;
    private Boolean emailNotifications = true;
    private Boolean pushNotifications = true;
    private Boolean smsNotifications = false;
    private Boolean marketingEmails = false;
}