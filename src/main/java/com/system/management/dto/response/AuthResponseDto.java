package com.system.management.dto.response;

import com.system.management.model.RoleEntity;

import java.util.List;
import java.util.UUID;

public class AuthResponseDto {

    private UUID id;
    private String token;
    private String email;
    private List<RoleEntity> authorities;

    public AuthResponseDto() {
    }

    public AuthResponseDto(UUID id, String token, String email, List<RoleEntity> authorities) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.authorities = authorities;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleEntity> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<RoleEntity> authorities) {
        this.authorities = authorities;
    }
}
