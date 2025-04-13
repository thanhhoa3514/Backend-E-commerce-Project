package com.project.e_commerce.repositories;

import com.project.e_commerce.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);

    User findByUserGoogleId(String userGgId);
    User findByUserFacebookId(String userFacebookId);

    Optional<User> findByUserEmail(String userEmail);

}
