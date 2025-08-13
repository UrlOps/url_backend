package be.url_backend.dto.response;

import lombok.Getter;

@Getter
public class JwtResponseDto {
    private final String token;

    public JwtResponseDto(String token) {
        this.token = token;
    }
} 