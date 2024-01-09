package com.system.management.helper;

import com.system.management.dto.request.AuthRequestDto;
import com.system.management.dto.request.UserRegisterRequestDto;
import com.system.management.dto.response.AuthResponseDto;
import com.system.management.dto.response.UserResponseDto;
import com.system.management.model.RoleEntity;
import com.system.management.model.UserEntity;
import com.system.management.model.enums.RoleEnum;
import java.util.Set;
import java.util.UUID;

public class UserTestHelper {

  private static final String TEST_EMAIL = "mail.mail.com";
  private static final String TEST_FIRST_NAME = "firstName";
  private static final String TEST_LAST_NAME = "lastName";
  private static final String TEST_PASSWORD = "password123@@";
  private static final String ENCODED_TEST_PASSWORD =
      "$2a$12$1Q9GQTAdh72fKZo7G2AiXe1uqyAUoGyO0GKEgbdTExoutgRHLq18i";

  public static UserEntity createUserWithId(UserRegisterRequestDto requestDto) {
    UserEntity user = new UserEntity();
    user.setId(UUID.randomUUID());
    user.setEmail(requestDto.getEmail());
    user.setFirstName(requestDto.getFirstName());
    user.setLastName(requestDto.getLastName());
    user.setPassword(requestDto.getPassword());
    user.setAuthorities(Set.of(new RoleEntity(RoleEnum.ADMIN)));
    return user;
  }

  public static UserRegisterRequestDto createUserRegisterRequestDto() {
    UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto();
    userRegisterRequestDto.setFirstName(TEST_FIRST_NAME);
    userRegisterRequestDto.setLastName(TEST_LAST_NAME);
    userRegisterRequestDto.setEmail(TEST_EMAIL);
    userRegisterRequestDto.setPassword(TEST_PASSWORD);
    userRegisterRequestDto.setConfirmPassword(TEST_PASSWORD);
    return userRegisterRequestDto;
  }

  public static UserResponseDto createUserResponseDto(UserEntity user) {
    UserResponseDto responseDto = new UserResponseDto();
    responseDto.setId(user.getId());
    responseDto.setFirstName(user.getFirstName());
    responseDto.setLastName(user.getLastName());
    responseDto.setEmail(user.getEmail());
    return responseDto;
  }

  public static AuthRequestDto createAuthRequestDto() {
    AuthRequestDto authRequestDto = new AuthRequestDto();
    authRequestDto.setEmail(TEST_EMAIL);
    authRequestDto.setPassword(ENCODED_TEST_PASSWORD);
    return authRequestDto;
  }

  public static AuthResponseDto createAuthResponseDto(UserEntity user, String token) {
    AuthResponseDto authResponseDto = new AuthResponseDto();
    authResponseDto.setToken(token);
    authResponseDto.setId(user.getId());
    authResponseDto.setEmail(user.getEmail());
    authResponseDto.setAuthorities(user.getAuthorities());
    return authResponseDto;
  }
}
