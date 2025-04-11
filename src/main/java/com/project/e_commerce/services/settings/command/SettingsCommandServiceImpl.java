package com.project.e_commerce.services.settings.command;

import com.project.e_commerce.dtos.setting.SettingsDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.user.PrivacySettings;
import com.project.e_commerce.models.user.Settings;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.SettingsRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.services.settings.mapper.ISettingsMapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class SettingsCommandServiceImpl implements ISettingsCommandService {
    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;
    private final ISettingsMapperService settingsMapperService;

    @Override
    public Settings createDefaultSettings(User user) {
        log.debug("Creating default settings for user ID: {}", user.getId());
        Settings settings = Settings.builder()
                .user(user)
                .language("en")
                .currency("USD")
                .theme("light")
                .enableTwoFactorAuth(false)
                .emailNotifications(true)
                .smsNotifications(false)
                .pushNotifications(true)
                .privacySettings(createDefaultPrivacySettings())
                .build();

        return settingsRepository.save(settings);
    }

    @Override
    @Transactional
    public Settings updateSettings(Long userId, SettingsDTO settingsDTO) {
        log.debug("Updating settings for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found with ID: " + userId));

        Settings settings = settingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(user));

        settingsMapperService.updateSettingsFromDTO(settings, settingsDTO);

        return settingsRepository.save(settings);
    }

    private PrivacySettings createDefaultPrivacySettings() {
        return PrivacySettings.builder()
                .showProfileToPublic(true)
                .showPurchaseHistory(false)
                .allowRecommendations(true)
                .shareActivityWithFriends(false)
                .allowDataCollection(true)
                .build();
    }
}
