package com.system.management.service.impl;

import com.system.management.dto.request.AuthRequestDto;
import com.system.management.dto.request.UserRegisterRequestDto;
import com.system.management.dto.response.AuthResponseDto;
import com.system.management.dto.response.UserResponseDto;
import com.system.management.exception.UserExistsException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  public AuthServiceImpl(
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      JwtUtils jwtUtils,
      ModelMapper modelMapper,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
    this.modelMapper = modelMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
    UserEntity user = getUserByEmail(authRequestDto.getEmail());

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
    checkForExistingUser(requestDto);

    String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
    checkForPasswordMatches(requestDto, encodedPassword);

    UserEntity user = createUserInDatabase(requestDto, encodedPassword);

    return modelMapper.map(user, UserResponseDto.class);
  }

  private UserEntity createUserInDatabase(
      UserRegisterRequestDto requestDto, String encodedPassword) {
    UserEntity user = new UserEntity();
    user.setEmail(requestDto.getEmail());
    user.setPassword(encodedPassword);
    user.setFirstName(requestDto.getFirstName());
    user.setLastName(requestDto.getLastName());
    userRepository.save(user);
    return user;
  }

  private void checkForPasswordMatches(UserRegisterRequestDto requestDto, String encodedPassword) {
    if (!passwordEncoder.matches(requestDto.getConfirmPassword(), encodedPassword)) {
      throw new InvalidCredentialsException();
    }
  }

  private UserEntity getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
  }

  private void checkForExistingUser(UserRegisterRequestDto userRegisterRequestDto) {
    UserEntity user = userRepository.findByEmail(userRegisterRequestDto.getEmail()).orElse(null);
    if (user != null) {
      throw new UserExistsException(userRegisterRequestDto.getEmail());
    }
  }
}
