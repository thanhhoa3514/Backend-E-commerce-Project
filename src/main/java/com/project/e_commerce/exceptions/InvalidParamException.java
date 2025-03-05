package com.project.e_commerce.exceptions;

import java.io.Serial;

public class InvalidParamException extends Exception{
    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidParamException(String message) {
        super(message);
    }

}
