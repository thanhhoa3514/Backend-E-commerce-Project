package com.project.e_commerce.services;

import com.project.e_commerce.dtos.UserDTO;
import com.project.e_commerce.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface IUserService {

    User createUser(UserDTO userDTO);
    User getUserById(long userId);
    String userLogin(String phone, String password);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
