package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.user.UserProfileDTO;
import com.project.e_commerce.services.user.profile.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final IUserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile() {
        return ResponseEntity.ok(userProfileService.getCurrentUserProfile());
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateCurrentUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        return ResponseEntity.ok(userProfileService.updateCurrentUserProfile(userProfileDTO));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDTO> getUserProfileByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getUserProfileByUserId(userId));
    }

    @PutMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDTO> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody UserProfileDTO userProfileDTO) {
        return ResponseEntity.ok(userProfileService.updateUserProfile(userId, userProfileDTO));
    }
}