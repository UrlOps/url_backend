package be.url_backend.common.dto;

import lombok.Getter;

@Getter
public class JwtResponseDto {
    private final String token;

    public JwtResponseDto(String token) {
        this.token = token;
    }
} 