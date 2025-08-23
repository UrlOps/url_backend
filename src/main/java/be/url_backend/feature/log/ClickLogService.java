package be.url_backend.feature.log;

import be.url_backend.feature.log.repository.ClickLogRepository;
import be.url_backend.feature.stats.DailyStats;
import be.url_backend.feature.url.UrlMapping;
import be.url_backend.feature.stats.repository.DailyStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
} 