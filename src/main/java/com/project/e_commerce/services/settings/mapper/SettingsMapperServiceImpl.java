package com.project.e_commerce.services.settings.mapper;



import com.project.e_commerce.dtos.setting.SettingsDTO;
import com.project.e_commerce.models.user.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SettingsMapperServiceImpl implements  ISettingsMapperService {
    @Override
    public SettingsDTO mapToDTO(Settings settings) {
        return SettingsDTO.builder()
                .language(settings.getLanguage())
                .currency(settings.getCurrency())
                .theme(settings.getTheme())
                .enableTwoFactorAuth(settings.isEnableTwoFactorAuth())
                .emailNotifications(settings.isEmailNotifications())
                .smsNotifications(settings.isSmsNotifications())
                .pushNotifications(settings.isPushNotifications())
                .privacySettings(settings.getPrivacySettings())
                .build();
    }

    @Override
    public void updateSettingsFromDTO(Settings settings, SettingsDTO dto) {
        if (dto.getLanguage() != null) {
            settings.setLanguage(dto.getLanguage());
        }
        if (dto.getCurrency() != null) {
            settings.setCurrency(dto.getCurrency());
        }
        if (dto.getTheme() != null) {
            settings.setTheme(dto.getTheme());
        }
        settings.setEnableTwoFactorAuth(dto.isEnableTwoFactorAuth());
        settings.setEmailNotifications(dto.isEmailNotifications());
        settings.setSmsNotifications(dto.isSmsNotifications());
        settings.setPushNotifications(dto.isPushNotifications());
        if (dto.getPrivacySettings() != null) {
            settings.setPrivacySettings(dto.getPrivacySettings());
        }
    }
}
