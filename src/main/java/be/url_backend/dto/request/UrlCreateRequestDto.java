package be.url_backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UrlCreateRequestDto {
    private String originalUrl;
    private String customKey;
} 