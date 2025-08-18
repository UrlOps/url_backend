package be.url_backend.dto.response;

import be.url_backend.domain.ClickLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClickLogResponseDto {
    private Long id;
    private String userAgent;
    private String ipAddress;
    private LocalDateTime createdAt;
    private String shortKey;
    private String originalUrl;

    public static ClickLogResponseDto from(ClickLog clickLog) {
        return ClickLogResponseDto.builder()
                .id(clickLog.getId())
                .userAgent(clickLog.getUserAgent())
                .ipAddress(clickLog.getIpAddress())
                .createdAt(clickLog.getCreatedAt())
                .shortKey(clickLog.getUrlMapping().getShortKey())
                .originalUrl(clickLog.getUrlMapping().getOriginalUrl())
                .build();
    }
} 