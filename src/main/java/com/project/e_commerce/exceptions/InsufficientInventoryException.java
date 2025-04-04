package com.project.e_commerce.exceptions;

public class InsufficientInventoryException extends RuntimeException {
  public InsufficientInventoryException(String message) {
    super(message);
  }
}
