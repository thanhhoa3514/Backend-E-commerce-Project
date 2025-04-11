package com.project.e_commerce.services.user.profile.mappers;

import com.project.e_commerce.dtos.user.NotificationPreferencesDTO;
import com.project.e_commerce.dtos.user.UserProfileDTO;
import com.project.e_commerce.models.user.NotificationPreferences;
import com.project.e_commerce.models.user.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserProfileMapperServiceImpl implements  IUserProfileMapperService {
    @Override
    public UserProfileDTO mapToDTO(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }

        UserProfileDTO dto = UserProfileDTO.builder()
                .id(userProfile.getId())
                .userId(userProfile.getUser().getId())
                .bio(userProfile.getBio())
                .dateOfBirth(userProfile.getDateOfBirth())
                .profilePicture(userProfile.getProfilePicture())
                .build();

        if (userProfile.getNotificationPreferences() != null) {
            NotificationPreferences prefs = userProfile.getNotificationPreferences();
            dto.setNotificationPreferences(NotificationPreferencesDTO.builder()
                    .id(prefs.getId())
                    .emailNotifications(prefs.getEmailNotifications())
                    .pushNotifications(prefs.getPushNotifications())
                    .smsNotifications(prefs.getSmsNotifications())
                    .marketingEmails(prefs.getMarketingEmails())
                    .build());
        }

        return dto;
    }

    @Override
    public void updateUserProfileFromDTO(UserProfile userProfile, UserProfileDTO userProfileDTO) {
        if (userProfileDTO == null) {
            return;
        }

        if (userProfileDTO.getBio() != null) {
            userProfile.setBio(userProfileDTO.getBio());
        }

        if (userProfileDTO.getDateOfBirth() != null) {
            userProfile.setDateOfBirth(userProfileDTO.getDateOfBirth());
        }

        if (userProfileDTO.getProfilePicture() != null) {
            userProfile.setProfilePicture(userProfileDTO.getProfilePicture());
        }

        if (userProfileDTO.getNotificationPreferences() != null) {
            NotificationPreferencesDTO prefsDTO = userProfileDTO.getNotificationPreferences();
            NotificationPreferences prefs = userProfile.getNotificationPreferences();

            if (prefs == null) {
                prefs = new NotificationPreferences();
                prefs.setUserProfile(userProfile);
                userProfile.setNotificationPreferences(prefs);
            }

            if (prefsDTO.getEmailNotifications() != null) {
                prefs.setEmailNotifications(prefsDTO.getEmailNotifications());
            }

            if (prefsDTO.getPushNotifications() != null) {
                prefs.setPushNotifications(prefsDTO.getPushNotifications());
            }

            if (prefsDTO.getSmsNotifications() != null) {
                prefs.setSmsNotifications(prefsDTO.getSmsNotifications());
            }

            if (prefsDTO.getMarketingEmails() != null) {
                prefs.setMarketingEmails(prefsDTO.getMarketingEmails());
            }
        }
    }
}
