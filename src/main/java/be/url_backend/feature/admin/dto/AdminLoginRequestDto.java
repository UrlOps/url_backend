package be.url_backend.feature.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginRequestDto {
    private String username;
    private String password;
} 