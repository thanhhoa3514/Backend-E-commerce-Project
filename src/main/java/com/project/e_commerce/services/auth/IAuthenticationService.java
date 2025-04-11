package com.project.e_commerce.services.auth;

import com.project.e_commerce.dtos.user.UserLoginDTO;
import com.project.e_commerce.dtos.user.UserRegisterDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface IAuthenticationService {
     Map<String, String> register(UserRegisterDTO registerDTO, HttpServletRequest request);
     Map<String, String> login(UserLoginDTO loginDTO, HttpServletRequest request);
    void logout(String token);
}
