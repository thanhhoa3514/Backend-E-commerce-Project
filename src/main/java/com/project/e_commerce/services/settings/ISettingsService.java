package com.project.e_commerce.services.settings;

import com.project.e_commerce.dtos.setting.SettingsDTO;

public interface ISettingsService {
    /**
     * Retrieves settings for a specific user
     * @param userId the ID of the user
     * @return the user's settings
     */
    SettingsDTO getUserSettings(Long userId);

    /**
     * Updates settings for a specific user
     * @param userId the ID of the user
     * @param settingsDTO the new settings
     * @return the updated settings
     */
    SettingsDTO updateSettings(Long userId, SettingsDTO settingsDTO);


    /**
     * Retrieves settings for the currently authenticated user
     * @return the current user's settings
     */
    SettingsDTO getCurrentUserSettings();

    /**
     * Updates settings for the currently authenticated user
     * @param settingsDTO the new settings
     * @return the updated settings
     */
    SettingsDTO updateCurrentUserSettings(SettingsDTO settingsDTO);
}
