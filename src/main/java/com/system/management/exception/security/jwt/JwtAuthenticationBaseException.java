package com.system.management.exception.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationBaseException extends AuthenticationException {
  public JwtAuthenticationBaseException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public JwtAuthenticationBaseException(String msg) {
    super(msg);
  }
}
