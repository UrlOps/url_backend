package be.url_backend.feature.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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