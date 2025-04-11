package com.project.e_commerce.services.user.mappers;

import com.project.e_commerce.dtos.user.UserDTO;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.responses.UserResponse;

public interface IUserMapperService {
    User mapToUser(UserDTO userDTO);
    UserResponse mapToUserResponse(User user);
    void updateUserFromDTO(User user, UserDTO userDTO);
}
