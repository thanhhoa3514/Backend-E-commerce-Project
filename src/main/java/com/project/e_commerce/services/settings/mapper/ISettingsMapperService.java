package com.project.e_commerce.services.settings.mapper;

import com.project.e_commerce.dtos.setting.SettingsDTO;
import com.project.e_commerce.models.user.Settings;


/**
 * Mapper service interface for converting between Settings entities and DTOs
 */
public interface ISettingsMapperService {

    /**
     * Maps a Settings entity to a SettingsDTO
     * @param settings the settings entity
     * @return the settings DTO
     */
    SettingsDTO mapToDTO(Settings settings);

    /**
     * Updates a Settings entity from a SettingsDTO
     * @param settings the settings entity to update
     * @param settingsDTO the DTO containing new values
     */
    void updateSettingsFromDTO(Settings settings, SettingsDTO settingsDTO);
}
