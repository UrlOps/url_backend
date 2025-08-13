package be.url_backend.dto.response;

import be.url_backend.domain.UrlMapping;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UrlResponseDto {
    private final Long id;
    private final String shortKey;
    private final String originalUrl;
    private final String shortenUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime expireAt;

    public static UrlResponseDto from(UrlMapping urlMapping, String baseUrl) {
        return UrlResponseDto.builder()
                .id(urlMapping.getId())
                .shortKey(urlMapping.getShortKey())
                .originalUrl(urlMapping.getOriginalUrl())
                .shortenUrl(baseUrl + "/" + urlMapping.getShortKey())
                .createdAt(urlMapping.getCreatedAt())
                .expireAt(urlMapping.getExpireAt())
                .build();
    }
} 