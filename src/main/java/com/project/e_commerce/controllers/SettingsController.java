package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.setting.SettingsDTO;

import com.project.e_commerce.services.settings.ISettingsService;
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

    private final ISettingsService settingsService;

    @GetMapping
    @Operation(summary = "Get current user settings", description = "Retrieves settings for the authenticated user")
    public ResponseEntity<SettingsDTO> getCurrentUserSettings() {
        return ResponseEntity.ok(settingsService.getCurrentUserSettings());
    }

    @PutMapping
    @Operation(summary = "Update current user settings", description = "Updates settings for the authenticated user")
    public ResponseEntity<SettingsDTO> updateCurrentUserSettings(@RequestBody SettingsDTO settingsDTO) {
        return ResponseEntity.ok(settingsService.updateCurrentUserSettings(settingsDTO));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user settings by ID", description = "Retrieves settings for a specific user (admin only)")
    public ResponseEntity<SettingsDTO> getUserSettings(@PathVariable Long userId) {
        return ResponseEntity.ok(settingsService.getUserSettings(userId));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user settings by ID", description = "Updates settings for a specific user (admin only)")
    public ResponseEntity<SettingsDTO> updateUserSettings(
            @PathVariable Long userId,
            @RequestBody SettingsDTO settingsDTO) {
        return ResponseEntity.ok(settingsService.updateSettings(userId, settingsDTO));
    }
}