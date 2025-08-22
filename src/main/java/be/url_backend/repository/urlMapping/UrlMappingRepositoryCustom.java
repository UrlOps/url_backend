package be.url_backend.repository.urlMapping;

import be.url_backend.dto.response.UrlResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UrlMappingRepositoryCustom {
    Page<UrlResponseDto> searchUrlMappings(Pageable pageable, String shortKey, String originalUrl, LocalDate startDate, LocalDate endDate);
} 