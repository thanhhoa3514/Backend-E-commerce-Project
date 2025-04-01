package com.project.e_commerce.services.user;

import com.project.e_commerce.dtos.user.UserDTO;
import com.project.e_commerce.models.User;

import java.util.Optional;

public interface IUserService {

    User createUser(UserDTO userDTO);
    User getUserById(long userId);
    String userLogin(String phone, String password) throws Exception;
    Optional<User> findByPhoneNumber(String phoneNumber);
}
