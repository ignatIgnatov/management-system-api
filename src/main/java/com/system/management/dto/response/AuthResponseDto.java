package com.system.management.dto.response;

import java.util.UUID;

public class AuthResponseDto {

    private UUID id;
    private String token;
    private String email;

    public AuthResponseDto() {
    }

    public AuthResponseDto(UUID id, String token, String email) {
        this.id = id;
        this.token = token;
        this.email = email;
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
}
