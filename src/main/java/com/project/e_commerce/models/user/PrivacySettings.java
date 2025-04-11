package com.project.e_commerce.models.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivacySettings {
    private boolean showProfileToPublic;
    private boolean showPurchaseHistory;
    private boolean allowRecommendations;
    private boolean shareActivityWithFriends;
    private boolean allowDataCollection;
}