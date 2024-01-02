package com.system.management.exception.security.jwt;

public class JwtIsNotValidException extends JwtAuthenticationBaseException {
  public JwtIsNotValidException() {
    super("Invalid JWT token");
  }

  public JwtIsNotValidException(Exception e) {
    super("Invalid JWT token: " + e.getMessage(), e.getCause());
  }
}
