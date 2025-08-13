package be.url_backend.dto.response;

import be.url_backend.domain.ClickLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClickLogResponseDto {

    private final Long id;
    private final String shortKey;
    private final LocalDateTime clickedAt;
    private final String userAgent;
    private final String country;

    public static ClickLogResponseDto from(ClickLog clickLog) {
        return ClickLogResponseDto.builder()
                .id(clickLog.getId())
                .shortKey(clickLog.getShortKey())
                .clickedAt(clickLog.getClickedAt())
                .userAgent(clickLog.getUserAgent())
                .country(clickLog.getCountry())
                .build();
    }
} 