package com.system.management.exception.not_found;

public class BaseNotFoundException extends RuntimeException {
  public BaseNotFoundException(String message) {
    super(message);
  }
}
