package com.project.e_commerce.repositories;

import com.project.e_commerce.enums.OTPType;
import com.project.e_commerce.models.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    void deleteByEmailAndType(String email, OTPType type);
    Optional<OTP> findByEmailAndTypeAndVerifiedFalse(String email, OTPType type);
}
