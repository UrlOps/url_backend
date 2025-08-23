package be.url_backend.feature.log;

import be.url_backend.feature.log.repository.ClickLogRepository;
import be.url_backend.feature.stats.DailyStats;
import be.url_backend.feature.stats.repository.DailyStatsRepository;
import be.url_backend.feature.url.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ClickLogWriter {

    private final ClickLogRepository clickLogRepository;
    private final DailyStatsRepository dailyStatsRepository;

    @Transactional
    public void saveLogAndStats(UrlMapping urlMapping, String userAgent, String ipAddress) {
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