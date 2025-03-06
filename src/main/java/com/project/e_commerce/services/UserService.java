package com.project.e_commerce.services;

import com.project.e_commerce.dtos.UserDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.GlobalExceptionHandler;
import com.project.e_commerce.models.Role;
import com.project.e_commerce.models.User;
import com.project.e_commerce.repositories.RoleRepository;
import com.project.e_commerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public User createUser(UserDTO userDTO) {

        String phoneNumber=userDTO.getPhoneNumber();


        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            throw new DataIntegrityViolationException("Password and retype password do not match");
        }


        User user=User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .isActive(true)
                .address(userDTO.getAddress())
                .email(userDTO.getEmail())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role=roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("Role not found with id: " + userDTO.getRoleId()));
        user.setRole(role);

        if(userDTO.getFacebookAccountId()==0&&userDTO.getGoogleAccountId()==0){
            String password=userDTO.getPassword();
//            String encodePassword=p

        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(()->new DataNotFoundException("User id not found "));
    }

    @Override
    public String login(String phone, String password) {
        return "";
    }
}
