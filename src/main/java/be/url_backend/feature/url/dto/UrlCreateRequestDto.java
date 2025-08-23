package be.url_backend.feature.url.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UrlCreateRequestDto {
    private String originalUrl;
} 