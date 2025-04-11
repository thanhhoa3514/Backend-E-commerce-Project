package com.project.e_commerce.dtos.user;



import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class UserProfileDTO {

    private Long id;
    private Long userId;
    private String bio;
    private LocalDate dateOfBirth;
    private String profilePicture;
    private NotificationPreferencesDTO notificationPreferences;
}