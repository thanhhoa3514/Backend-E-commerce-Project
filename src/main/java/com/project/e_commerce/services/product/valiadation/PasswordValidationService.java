package com.project.e_commerce.services.product.valiadation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class PasswordValidationService {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 100;
    private static final Pattern HAS_UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern HAS_LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern HAS_NUMBER = Pattern.compile("\\d");
    private static final Pattern HAS_SPECIAL_CHAR = Pattern.compile("[^A-Za-z0-9]");

    public List<String> validatePassword(String password) {
        List<String> validationErrors = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            validationErrors.add("Password cannot be empty");
            return validationErrors;
        }

        if (password.length() < MIN_LENGTH) {
            validationErrors.add("Password must be at least " + MIN_LENGTH + " characters long");
        }

        if (password.length() > MAX_LENGTH) {
            validationErrors.add("Password must be less than " + MAX_LENGTH + " characters");
        }

        if (!HAS_UPPERCASE.matcher(password).find()) {
            validationErrors.add("Password must contain at least one uppercase letter");
        }

        if (!HAS_LOWERCASE.matcher(password).find()) {
            validationErrors.add("Password must contain at least one lowercase letter");
        }

        if (!HAS_NUMBER.matcher(password).find()) {
            validationErrors.add("Password must contain at least one number");
        }

        if (!HAS_SPECIAL_CHAR.matcher(password).find()) {
            validationErrors.add("Password must contain at least one special character");
        }

        return validationErrors;
    }

    public boolean isPasswordValid(String password) {
        return validatePassword(password).isEmpty();
    }
}
