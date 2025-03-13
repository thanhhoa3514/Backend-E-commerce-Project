package com.project.e_commerce.services.user.queries;

import com.project.e_commerce.models.User;

import java.util.Optional;

public interface IUserQueryService {
    User getUserById(long userId);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
