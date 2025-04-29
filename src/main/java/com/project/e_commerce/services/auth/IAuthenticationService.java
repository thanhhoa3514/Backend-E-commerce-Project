package com.project.e_commerce.services.auth;

import com.project.e_commerce.dtos.user.UserLoginDTO;
import com.project.e_commerce.dtos.user.UserRegisterDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.user.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface IAuthenticationService {
    User register(UserRegisterDTO registerDTO, HttpServletRequest request);
    Map<String, String> login(UserLoginDTO loginDTO, HttpServletRequest request);
    void logout(String token);
    Map<String, String> refreshToken(String refreshToken) throws DataNotFoundException;
    boolean updatePassword(String email, String newPassword);
}
