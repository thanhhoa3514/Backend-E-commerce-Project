package com.project.e_commerce.services.settings.queries;

import com.project.e_commerce.models.user.Settings;

public interface ISettingsQueryService {


    /**
     * Gets settings for a user
     * @param userId the ID of the user
     * @return the user's settings
     */
    Settings getSettingsByUserId(Long userId);
}
