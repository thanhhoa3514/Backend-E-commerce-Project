package com.project.e_commerce.services.user.profile;

import com.project.e_commerce.dtos.user.UserProfileDTO;

public interface IUserProfileService {
    UserProfileDTO getUserProfileByUserId(Long userId);
    UserProfileDTO updateUserProfile(Long userId, UserProfileDTO userProfileDTO);
    UserProfileDTO getCurrentUserProfile();
    UserProfileDTO updateCurrentUserProfile(UserProfileDTO userProfileDTO);
}
