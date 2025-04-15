package com.project.e_commerce.repositories;

import com.project.e_commerce.models.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    SocialAccount findByProviderAndProviderId(String provider, Long providerId);
    
    Optional<SocialAccount> findByEmailAndProvider(String email, String provider);
}
