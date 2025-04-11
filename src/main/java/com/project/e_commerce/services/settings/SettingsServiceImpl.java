package com.project.e_commerce.services.settings;


import com.project.e_commerce.dtos.setting.SettingsDTO;

import com.project.e_commerce.models.user.User;
import com.project.e_commerce.models.user.PrivacySettings;
import com.project.e_commerce.models.user.Settings;
import com.project.e_commerce.repositories.SettingsRepository;
import com.project.e_commerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements  ISettingsService{

    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;

    private Settings createDefaultSettings(com.project.e_commerce.models.user.User user) {
        Settings settings = Settings.builder()
                .user(user)
                .language("en")
                .currency("USD")
                .theme("light")
                .enableTwoFactorAuth(false)
                .emailNotifications(true)
                .smsNotifications(false)
                .pushNotifications(true)
                .privacySettings(PrivacySettings.builder()
                        .showProfileToPublic(true)
                        .showPurchaseHistory(false)
                        .allowRecommendations(true)
                        .shareActivityWithFriends(false)
                        .allowDataCollection(true)
                        .build())
                .build();

        return settingsRepository.save(settings);
    }

    private com.project.e_commerce.dtos.setting.SettingsDTO mapToDTO(Settings settings) {
        return com.project.e_commerce.dtos.setting.SettingsDTO.builder()
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
    public SettingsDTO getUserSettings(Long userId) {
        com.project.e_commerce.models.user.User currentUser = (com.project.e_commerce.models.user.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Settings settings = settingsRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> createDefaultSettings(currentUser));

        return mapToDTO(settings);
    }

    @Override
    @Transactional
    public SettingsDTO updateSettings(Long userId, SettingsDTO settingsDTO) {
        com.project.e_commerce.models.user.User currentUser = (com.project.e_commerce.models.user.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Settings settings = settingsRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> createDefaultSettings(currentUser));

        // Update settings
        settings.setLanguage(settingsDTO.getLanguage());
        settings.setCurrency(settingsDTO.getCurrency());
        settings.setTheme(settingsDTO.getTheme());
        settings.setEnableTwoFactorAuth(settingsDTO.isEnableTwoFactorAuth());
        settings.setEmailNotifications(settingsDTO.isEmailNotifications());
        settings.setSmsNotifications(settingsDTO.isSmsNotifications());
        settings.setPushNotifications(settingsDTO.isPushNotifications());
        settings.setPrivacySettings(settingsDTO.getPrivacySettings());

        Settings savedSettings = settingsRepository.save(settings);
        return mapToDTO(savedSettings);
    }
}