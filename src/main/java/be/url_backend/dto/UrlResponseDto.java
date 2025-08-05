package be.url_backend.dto;

import be.url_backend.domain.UrlMapping;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UrlResponseDto {
    private final Long id;
    private final String shortKey;
    private final String originalUrl;
    private final String shortenUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime expireAt;

    public UrlResponseDto(UrlMapping urlMapping, String baseUrl) {
        this.id = urlMapping.getId();
        this.shortKey = urlMapping.getShortKey();
        this.originalUrl = urlMapping.getOriginalUrl();
        this.shortenUrl = baseUrl + "/" + urlMapping.getShortKey();
        this.createdAt = urlMapping.getCreatedAt();
        this.expireAt = urlMapping.getExpireAt();
    }
}
