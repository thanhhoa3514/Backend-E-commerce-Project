package com.project.e_commerce.services.user.validation;


import com.project.e_commerce.dtos.UserDTO;
import com.project.e_commerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final UserRepository userRepository;

    public void validateNewUser(UserDTO userDTO) {
        validatePhoneNumber(userDTO.getPhoneNumber());
        validatePassword(userDTO.getPassword(), userDTO.getRetypePassword());
    }

    public void validatePhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
    }

    public void validatePassword(String password, String retypePassword) {
        if (!password.equals(retypePassword)) {
            throw new DataIntegrityViolationException("Password and retype password do not match");
        }
    }

}
