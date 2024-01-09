package com.system.management.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.system.management.dto.request.AuthRequestDto;
import com.system.management.dto.request.UserRegisterRequestDto;
import com.system.management.dto.response.AuthResponseDto;
import com.system.management.dto.response.UserResponseDto;
import com.system.management.exception.UserExistsException;
import com.system.management.exception.not_found.UserNotFoundException;
import com.system.management.exception.security.InvalidCredentialsException;
import com.system.management.helper.UserTestHelper;
import com.system.management.model.UserEntity;
import com.system.management.repository.UserRepository;
import com.system.management.security.jwt.JwtUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  private static final String ENCODED_TEST_PASSWORD =
      "$2a$12$1Q9GQTAdh72fKZo7G2AiXe1uqyAUoGyO0GKEgbdTExoutgRHLq18i";
  private static final String TEST_TOKEN =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhYnYuYmciLCJpYXQiOjE3MDQ4MDQ0MjcsImV4cCI6MTcwNDg5MDgyN30.dyHjBt8YpIaHrbaYwHoofHy2VIF52mrF76GKICxR5W8";

  @InjectMocks private AuthServiceImpl authService;
  @Mock private UserRepository userRepository;
  @Mock private BCryptPasswordEncoder passwordEncoder;
  @Mock private ModelMapper modelMapper;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtUtils jwtUtils;

  private UserRegisterRequestDto userRegisterRequestDto;
  private UserEntity user;
  private AuthRequestDto authRequestDto;

  @BeforeEach
  void setUp() {
    userRegisterRequestDto = UserTestHelper.createUserRegisterRequestDto();
    user = UserTestHelper.createUserWithId(userRegisterRequestDto);
    authRequestDto = UserTestHelper.createAuthRequestDto();
  }

  @Test
  void testRegisterUserShouldThrowWhenEmailExists() {
    when(userRepository.findByEmail(userRegisterRequestDto.getEmail()))
        .thenReturn(Optional.of(user));
    assertThrows(UserExistsException.class, () -> authService.register(userRegisterRequestDto));
  }

  @Test
  void testRegisterUserShouldThrowWhenPasswordNotMatches() {
    assertThrows(
        InvalidCredentialsException.class, () -> authService.register(userRegisterRequestDto));
  }

  @Test
  void testRegisterUserSuccessfully() {
    when(passwordEncoder.encode(ArgumentMatchers.any())).thenReturn(ENCODED_TEST_PASSWORD);
    when(passwordEncoder.matches(
            userRegisterRequestDto.getConfirmPassword(), ENCODED_TEST_PASSWORD))
        .thenReturn(true);

    UserResponseDto actualResponse = authService.register(userRegisterRequestDto);

    verify(userRepository, times(1)).findByEmail(userRegisterRequestDto.getEmail());
    verify(userRepository, times(1)).save(ArgumentMatchers.any());
    verify(modelMapper, times(1)).map(ArgumentMatchers.any(), ArgumentMatchers.any());
  }

  @Test
  void testLoginShouldThrowWhenUserNotExists() {
    assertThrows(UserNotFoundException.class, () -> authService.authenticate(authRequestDto));
  }

  @Test
  void testLoginShouldThrowWhenWrongPassword() {
    authRequestDto.setPassword("wrong password");
    when(authenticationManager.authenticate(any())).thenThrow(new InvalidCredentialsException());
    when(userRepository.findByEmail(authRequestDto.getEmail())).thenReturn(Optional.of(user));

    assertThrows(InvalidCredentialsException.class, () -> authService.authenticate(authRequestDto));
  }

  @Test
  void testLoginSuccessfully() {
    when(userRepository.findByEmail(userRegisterRequestDto.getEmail()))
        .thenReturn(Optional.of(user));
    when(authenticationManager.authenticate(any())).thenReturn(null);
    when(jwtUtils.generateToken(user)).thenReturn(TEST_TOKEN);

    AuthResponseDto authResponseDto = UserTestHelper.createAuthResponseDto(user, TEST_TOKEN);
    when(modelMapper.map(user, AuthResponseDto.class)).thenReturn(authResponseDto);

    AuthResponseDto actualResponse = authService.authenticate(authRequestDto);

    assertEquals(authResponseDto, actualResponse);
    assertEquals(TEST_TOKEN, actualResponse.getToken());
    verify(authenticationManager)
        .authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequestDto.getEmail(), authRequestDto.getPassword()));
    verify(userRepository).findByEmail(authRequestDto.getEmail());
    verify(jwtUtils).generateToken(user);
    verify(modelMapper).map(user, AuthResponseDto.class);
  }
}
