package com.project.e_commerce.services.user.commands;



import com.project.e_commerce.dtos.user.UserDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Role;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.RoleRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.services.jwt.JwtServiceImpl;
import com.project.e_commerce.services.user.mappers.IUserMapperService;
import com.project.e_commerce.services.user.validation.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements IUserCommandService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
//    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final IUserMapperService userMapperService;
    private final UserValidationService userValidationService;
    @Override
    public User createUser(UserDTO userDTO) {

        userValidationService.validateNewUser(userDTO);

        User user = userMapperService.mapToUser(userDTO);
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found with id: " + userDTO.getRoleId()));
        user.setRole(role);

        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String encodePassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encodePassword);
        }

        return userRepository.save(user);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        // Log để debug
        System.out.println("Attempting login for phone number: " + phoneNumber);
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("Invalid phone number or password"));

        if (user.getGoogleAccountId() == 0 && user.getFacebookAccountId() == 0) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        // Log thông tin user trước khi tạo token
        System.out.println("User found: " + user.getPhoneNumber());
        System.out.println("User role: " + user.getRole().getName());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(phoneNumber, password);
        authenticationManager.authenticate(authenticationToken);

        return jwtService.generateAccessToken(user);
    }
}
