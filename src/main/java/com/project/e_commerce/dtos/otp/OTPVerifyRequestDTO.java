package com.project.e_commerce.dtos.otp;


import com.project.e_commerce.enums.OTPType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPVerifyRequestDTO {
    @NotBlank(message = "OTP code is required")
    private String code;

    @NotNull(message = "OTP type is required")
    private OTPType type;
}
