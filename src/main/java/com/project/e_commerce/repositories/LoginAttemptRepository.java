package com.project.e_commerce.repositories;

import com.project.e_commerce.models.LoginAttempt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginAttemptRepository extends CrudRepository<LoginAttempt, Long> {
    @Query("SELECT COUNT(l) FROM LoginAttempt l WHERE l.phoneNumber = ?1 AND l.successful = false AND l.attemptTime > ?2")
    int countFailedAttempts(String phoneNumber, LocalDateTime since);

    @Query("SELECT COUNT(l) FROM LoginAttempt l WHERE l.ipAddress = ?1 AND l.successful = false AND l.attemptTime > ?2")
    int countFailedAttemptsByIp(String ipAddress, LocalDateTime since);

    List<LoginAttempt> findByPhoneNumberOrderByAttemptTimeDesc(String phoneNumber);
}
