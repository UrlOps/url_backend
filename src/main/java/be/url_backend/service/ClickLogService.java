package be.url_backend.service;

import be.url_backend.domain.ClickLog;
import be.url_backend.domain.DailyStats;
import be.url_backend.domain.UrlMapping;
import be.url_backend.dto.response.ClickLogResponseDto;
import be.url_backend.repository.ClickLogRepository;
import be.url_backend.repository.DailyStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClickLogService {

    private final ClickLogRepository clickLogRepository;
    private final DailyStatsRepository dailyStatsRepository;

    @Async
    @Transactional
    public void logClickAndupdateDailyStats(UrlMapping urlMapping, String userAgent, String ipAddress) {
        ClickLog clickLog = new ClickLog(urlMapping, userAgent, ipAddress);
        clickLogRepository.save(clickLog);

        updateDailyStats(urlMapping);
    }

    private void updateDailyStats(UrlMapping urlMapping) {
        LocalDate today = LocalDate.now();
        DailyStats dailyStats = dailyStatsRepository.findByUrlMappingAndDate(urlMapping, today)
                .orElse(new DailyStats(urlMapping, today));

        dailyStats.incrementClickCount();
        dailyStatsRepository.save(dailyStats);
    }

    public List<ClickLogResponseDto> getClickLogsByIpForPeriod(String ipAddress, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        List<ClickLog> clickLogs = clickLogRepository.findByIpAddressAndCreatedAtBetweenWithUrlMapping(ipAddress, start, end);

        return clickLogs.stream()
                .map(ClickLogResponseDto::from)
                .collect(Collectors.toList());
    }
} 