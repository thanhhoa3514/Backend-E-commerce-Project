package com.project.e_commerce.services.user.profile.mappers;

import com.project.e_commerce.dtos.user.UserProfileDTO;
import com.project.e_commerce.models.user.UserProfile;

public interface IUserProfileMapperService {
    UserProfileDTO mapToDTO(UserProfile userProfile);
    void updateUserProfileFromDTO(UserProfile userProfile, UserProfileDTO userProfileDTO);
}
