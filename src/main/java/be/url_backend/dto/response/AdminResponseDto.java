package be.url_backend.dto.response;

import be.url_backend.domain.Admin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminResponseDto {
    private final Long id;
    private final String username;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static AdminResponseDto from(Admin admin) {
        return AdminResponseDto.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }
} 