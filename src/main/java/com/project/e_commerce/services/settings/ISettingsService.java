package com.project.e_commerce.services.settings;

import com.project.e_commerce.dtos.setting.SettingsDTO;

public interface ISettingsService {
    SettingsDTO getUserSettings(Long userId);
    SettingsDTO updateSettings(Long userId, SettingsDTO settingsDTO);
}
