package com.system.management.exception.security.jwt;

public class JwtExpiredException extends JwtAuthenticationBaseException {
  public JwtExpiredException() {
    super("JWT token has expired");
  }
}
