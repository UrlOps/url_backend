package be.url_backend.dto.response;

import be.url_backend.domain.ClickLog;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClickLogResponseDto {

    private final Long id;
    private final String shortKey;
    private final LocalDateTime createdAt;
    private final String userAgent;
    private final String ipaddress;

    public static ClickLogResponseDto from(ClickLog clickLog) {
        return ClickLogResponseDto.builder()
                .id(clickLog.getId())
                .shortKey(clickLog.getUrlMapping().getShortKey())
                .createdAt(clickLog.getCreatedAt())
                .userAgent(clickLog.getUserAgent())
                .ipaddress(clickLog.getIpAddress())
                .build();
    }
} 