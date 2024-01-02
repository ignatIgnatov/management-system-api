package com.system.management.exception.security;

import com.system.management.exception.security.jwt.JwtAuthenticationBaseException;

public class InvalidCredentialsException extends JwtAuthenticationBaseException {
  public InvalidCredentialsException() {
    super("Invalid email or password");
  }
}
