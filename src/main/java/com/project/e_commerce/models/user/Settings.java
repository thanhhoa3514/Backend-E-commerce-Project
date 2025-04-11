package com.project.e_commerce.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_settings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "language")
    private String language;

    @Column(name = "currency")
    private String currency;

    @Column(name = "theme")
    private String theme;

    @Column(name = "enable_two_factor_auth")
    private boolean enableTwoFactorAuth;

    @Column(name = "email_notifications")
    private boolean emailNotifications;

    @Column(name = "sms_notifications")
    private boolean smsNotifications;

    @Column(name = "push_notifications")
    private boolean pushNotifications;

    @Column(name = "privacy_settings")
    @Convert(converter = PrivacySettingsConverter.class)
    private PrivacySettings privacySettings;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}