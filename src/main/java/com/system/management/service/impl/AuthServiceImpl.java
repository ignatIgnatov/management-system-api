package com.system.management.service.impl;

import com.system.management.dto.request.AuthRequestDto;
import com.system.management.dto.request.UserRegisterRequestDto;
import com.system.management.dto.response.AuthResponseDto;
import com.system.management.dto.response.UserResponseDto;
import com.system.management.exception.not_found.UserNotFoundException;
import com.system.management.exception.security.InvalidCredentialsException;
import com.system.management.exception.security.jwt.JwtAuthenticationBaseException;
import com.system.management.model.UserEntity;
import com.system.management.repository.UserRepository;
import com.system.management.security.jwt.JwtUtils;
import com.system.management.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final ModelMapper modelMapper;

  public AuthServiceImpl(
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      JwtUtils jwtUtils,
      ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
    this.modelMapper = modelMapper;
  }

  @Override
  public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
    UserEntity user = getUserByEmail(authRequestDto);

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authRequestDto.getEmail(), authRequestDto.getPassword()));
    } catch (JwtAuthenticationBaseException exception) {
      throw new InvalidCredentialsException();
    }

    String token = jwtUtils.generateToken(user);

    AuthResponseDto authResponseDto = modelMapper.map(user, AuthResponseDto.class);
    authResponseDto.setToken(token);

    return authResponseDto;
  }

  @Override
  public UserResponseDto register(UserRegisterRequestDto requestDto) {
    return null;
  }

  private UserEntity getUserByEmail(AuthRequestDto authRequestDto) {
    return userRepository
        .findByEmail(authRequestDto.getEmail())
        .orElseThrow(() -> new UserNotFoundException(authRequestDto.getEmail()));
  }
}
