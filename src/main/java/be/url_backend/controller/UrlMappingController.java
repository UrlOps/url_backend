package be.url_backend.controller;

import be.url_backend.domain.UrlMapping;
import be.url_backend.dto.UrlCreateRequestDto;
import be.url_backend.dto.UrlResponseDto;
import be.url_backend.service.UrlMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/urls")
public class UrlMappingController {

    private final UrlMappingService urlMappingService;

    private final String BASE_URL = "http://localhost:8080/r";

    @PostMapping
    public ResponseEntity<UrlResponseDto> createShortUrl(@RequestBody UrlCreateRequestDto request) {
        UrlMapping urlMapping = urlMappingService.createShortUrl(request);
        UrlResponseDto response = new UrlResponseDto(urlMapping, BASE_URL);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<UrlResponseDto> getUrlInfo(@PathVariable String shortKey) {
        UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortKey);
        UrlResponseDto response = new UrlResponseDto(urlMapping, BASE_URL);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{shortKey}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortKey) {
        urlMappingService.deleteUrl(shortKey);
        return ResponseEntity.noContent().build();
    }
}
