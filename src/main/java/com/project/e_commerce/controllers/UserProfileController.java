package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.user.UserProfileDTO;
import com.project.e_commerce.services.user.profile.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("${api.prefix}/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final IUserProfileService userProfileService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Get current user profile", 
              description = "Retrieves the profile of the currently authenticated user",
              security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile() {
        return ResponseEntity.ok(userProfileService.getCurrentUserProfile());
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Update current user profile", 
              description = "Updates the profile of the currently authenticated user",
              security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<UserProfileDTO> updateCurrentUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        return ResponseEntity.ok(userProfileService.updateCurrentUserProfile(userProfileDTO));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user profile by user ID", 
              description = "Retrieves the profile of a user by their ID",
              security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<UserProfileDTO> getUserProfileByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getUserProfileByUserId(userId));
    }

    @PutMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user profile by user ID", 
              description = "Updates the profile of a user by their ID",
              security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<UserProfileDTO> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody UserProfileDTO userProfileDTO) {
        return ResponseEntity.ok(userProfileService.updateUserProfile(userId, userProfileDTO));
    }
}