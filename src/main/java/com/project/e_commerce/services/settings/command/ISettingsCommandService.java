package com.project.e_commerce.services.settings.command;

import com.project.e_commerce.dtos.setting.SettingsDTO;
import com.project.e_commerce.models.user.Settings;
import com.project.e_commerce.models.user.User;

public interface ISettingsCommandService {
    /**
     * Creates default settings for a user
     * @param user the user to create settings for
     * @return the created settings
     */
    Settings createDefaultSettings(User user);

    /**
     * Updates settings for a user
     * @param userId the ID of the user
     * @param settingsDTO the new settings
     * @return the updated settings
     */
    Settings updateSettings(Long userId, SettingsDTO settingsDTO);
}
