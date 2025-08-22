package be.url_backend.repository.dailyState;

import be.url_backend.dto.response.DailyStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;

public interface DailyStateRepositoryCustom {
    Page<DailyStatsDto> searchDailyStats(Pageable pageable, LocalDate startDate, LocalDate endDate);
}
