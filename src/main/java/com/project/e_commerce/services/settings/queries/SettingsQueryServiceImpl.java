package com.project.e_commerce.services.settings.queries;

import com.project.e_commerce.models.user.Settings;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.SettingsRepository;
import com.project.e_commerce.services.settings.command.ISettingsCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class SettingsQueryServiceImpl implements  ISettingsQueryService{
    private final SettingsRepository settingsRepository;
    private final ISettingsCommandService settingsCommandService;

    @Override
    public Settings getSettingsByUserId(Long userId) {
        log.debug("Fetching settings for user ID: {}", userId);
        return settingsRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = new User();
                    user.setId(userId);
                    return settingsCommandService.createDefaultSettings(user);
                });
    }
}
