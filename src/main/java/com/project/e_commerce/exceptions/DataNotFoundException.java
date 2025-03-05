package com.project.e_commerce.exceptions;

public class DataNotFoundException extends RuntimeException  {
    public DataNotFoundException(String message) {
        super(message);
    }
}
