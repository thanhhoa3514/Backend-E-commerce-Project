package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.setting.SettingsDTO;

import com.project.e_commerce.services.settings.SettingsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "User settings management APIs")
public class SettingsController {

    private final SettingsServiceImpl settingsService;

    @GetMapping
    @Operation(summary = "Get user settings", description = "Retrieves settings for the authenticated user")
    public ResponseEntity<com.project.e_commerce.dtos.setting.SettingsDTO> getUserSettings() {
        return ResponseEntity.ok(settingsService.getUserSettings());
    }

    @PutMapping
    @Operation(summary = "Update user settings", description = "Updates settings for the authenticated user")
    public ResponseEntity<SettingsDTO> updateSettings(@RequestBody SettingsDTO settingsDTO) {
        return ResponseEntity.ok(settingsService.updateSettings(settingsDTO));
    }
}