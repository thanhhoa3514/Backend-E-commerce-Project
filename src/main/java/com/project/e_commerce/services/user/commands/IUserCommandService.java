package com.project.e_commerce.services.user.commands;

import com.project.e_commerce.dtos.user.UserDTO;
import com.project.e_commerce.models.user.User;

public interface IUserCommandService {
    User createUser(UserDTO userDTO);
    String login(String phoneNumber, String password) throws Exception;
}
