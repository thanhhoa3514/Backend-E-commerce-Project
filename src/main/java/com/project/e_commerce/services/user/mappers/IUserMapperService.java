package com.project.e_commerce.services.user.mappers;

import com.project.e_commerce.dtos.UserDTO;
import com.project.e_commerce.models.User;
import com.project.e_commerce.responses.UserResponse;

public interface IUserMapperService {
    User mapToUser(UserDTO userDTO);
    UserResponse mapToUserResponse(User user);
    void updateUserFromDTO(User user, UserDTO userDTO);
}
