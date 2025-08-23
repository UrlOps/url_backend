package be.url_backend.feature.log.repository;

import be.url_backend.feature.log.ClickLogResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ClickLogRepositoryCustom {
    Page<ClickLogResponseDto> searchClickLogs(Pageable pageable, String ipAddress, LocalDate startDate, LocalDate endDate);
} 