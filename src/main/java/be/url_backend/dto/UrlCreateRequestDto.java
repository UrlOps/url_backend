package be.url_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UrlCreateRequestDto {
    private String originalUrl;
    private String customKey;
}
