package com.system.management.dto.request;

import jakarta.validation.constraints.*;

public class UserRegisterRequestDto {

  private static final String PWD_PATTERN_VALIDATION_MSG =
      "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and be at least 8 characters long";

  private String firstName;
  private String lastName;
  @Email private String email;

  @NotBlank(message = "Password must not be blank, empty or null")
  @Size(min = 8, message = "Password must be at least 8 characters")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
      message = PWD_PATTERN_VALIDATION_MSG)
  @NotNull
  private String password;

  @NotNull private String confirmPassword;

  public UserRegisterRequestDto() {}

  public UserRegisterRequestDto(
      String firstName, String lastName, String email, String password, String confirmPassword) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.confirmPassword = confirmPassword;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }
}
