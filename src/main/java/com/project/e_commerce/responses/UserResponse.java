package com.project.e_commerce.responses;


import com.project.e_commerce.models.Role;
import com.project.e_commerce.models.user.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String email;
    private LocalDate dateOfBirth;
    private Long roleId;
    private boolean isActive;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .isActive(user.getIsActive())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .build();
    }
}
