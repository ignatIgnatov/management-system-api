package com.system.management.service;

import com.system.management.dto.request.AuthRequestDto;
import com.system.management.dto.request.UserRegisterRequestDto;
import com.system.management.dto.response.AuthResponseDto;
import com.system.management.dto.response.UserResponseDto;

public interface AuthService {
    AuthResponseDto authenticate(AuthRequestDto authRequestDto);
    UserResponseDto register(UserRegisterRequestDto requestDto);
}
