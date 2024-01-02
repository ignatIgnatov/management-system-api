package com.system.management.exception.not_found;

public class UserNotFoundException extends BaseNotFoundException {
  public UserNotFoundException(String email) {
    super("User with email " + email + " not found!");
  }
}
