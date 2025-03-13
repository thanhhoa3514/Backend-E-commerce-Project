package com.project.e_commerce.services.user.commands;

import com.project.e_commerce.dtos.UserDTO;
import com.project.e_commerce.models.User;

public interface IUserCommandService {
    User createUser(UserDTO userDTO);
    String login(String phoneNumber, String password) throws Exception;
}
