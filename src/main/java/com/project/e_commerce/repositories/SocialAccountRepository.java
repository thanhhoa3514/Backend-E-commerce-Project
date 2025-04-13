package com.project.e_commerce.repositories;

import com.project.e_commerce.models.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    SocialAccount findByProviderAndProviderId(String provider, Long providerId);
}
