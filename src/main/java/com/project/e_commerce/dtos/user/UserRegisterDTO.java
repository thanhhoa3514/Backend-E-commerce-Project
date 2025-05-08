package com.project.e_commerce.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    @JsonProperty("fullname")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @JsonProperty("retype_password")
    @NotBlank(message = "Retype password is required")
    private String retypePassword;

    @NotBlank(message = "Address is required")
    private String address;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("facebook_account_id")
    private long facebookAccountId;

    @JsonProperty("google_account_id")
    private long googleAccountId;

    @JsonProperty("role_id")
    @Min(value = 1, message = "Role ID must be greater than 0")
    private Long roleId;
}
