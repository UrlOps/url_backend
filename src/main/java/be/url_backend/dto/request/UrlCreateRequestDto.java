package be.url_backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UrlCreateRequestDto {
    private String originalUrl;
} 