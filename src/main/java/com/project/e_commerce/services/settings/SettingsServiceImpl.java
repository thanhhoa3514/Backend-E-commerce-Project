package com.project.e_commerce.services.settings;


import com.project.e_commerce.dtos.setting.SettingsDTO;

import com.project.e_commerce.models.user.User;
import com.project.e_commerce.models.user.PrivacySettings;
import com.project.e_commerce.models.user.Settings;
import com.project.e_commerce.repositories.SettingsRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.services.settings.command.ISettingsCommandService;
import com.project.e_commerce.services.settings.mapper.ISettingsMapperService;
import com.project.e_commerce.services.settings.queries.ISettingsQueryService;
import com.project.e_commerce.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettingsServiceImpl implements  ISettingsService{

    private final ISettingsCommandService settingsCommandService;
    private final ISettingsQueryService settingsQueryService;
    private final ISettingsMapperService settingsMapperService;
    private final IUserService userService;


    @Override
    public SettingsDTO getUserSettings(Long userId) {
        log.debug("Getting settings for user ID: {}", userId);
        Settings settings = settingsQueryService.getSettingsByUserId(userId);
        return settingsMapperService.mapToDTO(settings);
    }

    @Override
    @Transactional
    public SettingsDTO updateSettings(Long userId, SettingsDTO settingsDTO) {
        log.debug("Updating settings for user ID: {}", userId);
        Settings updatedSettings = settingsCommandService.updateSettings(userId, settingsDTO);
        return settingsMapperService.mapToDTO(updatedSettings);
    }

    @Override
    public SettingsDTO getCurrentUserSettings() {
        User currentUser = userService.getCurrentUser();
        return getUserSettings(currentUser.getId());
    }

    @Override
    @Transactional
    public SettingsDTO updateCurrentUserSettings(SettingsDTO settingsDTO) {
        User currentUser = userService.getCurrentUser();
        return updateSettings(currentUser.getId(), settingsDTO);
    }
}