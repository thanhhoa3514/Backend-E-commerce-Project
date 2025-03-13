package com.project.e_commerce.responses;


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
    private String roleName; // Thêm tên role để frontend dễ sử dụng
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
