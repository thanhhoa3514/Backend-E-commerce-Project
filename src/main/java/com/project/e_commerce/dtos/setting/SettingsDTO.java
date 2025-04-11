package com.project.e_commerce.dtos.setting;

import com.project.e_commerce.models.user.PrivacySettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDTO {
    private String language;
    private String currency;
    private String theme;
    private boolean enableTwoFactorAuth;
    private boolean emailNotifications;
    private boolean smsNotifications;
    private boolean pushNotifications;
    private PrivacySettings privacySettings;
}