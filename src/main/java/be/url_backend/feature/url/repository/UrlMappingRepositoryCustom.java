package be.url_backend.feature.url.repository;

import be.url_backend.feature.url.dto.UrlResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UrlMappingRepositoryCustom {
    Page<UrlResponseDto> searchUrlMappings(Pageable pageable, String shortKey, String originalUrl, LocalDate startDate, LocalDate endDate);
} 