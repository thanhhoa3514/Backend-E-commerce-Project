package com.project.e_commerce.exceptions;

import java.io.IOException;

public class ResourceNotFoundException extends IOException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
