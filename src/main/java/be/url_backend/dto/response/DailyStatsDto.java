package be.url_backend.dto.response;

import be.url_backend.domain.DailyStats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStatsDto {
    private LocalDate date;
    private Long count;

    public static DailyStatsDto from(DailyStats dailyStats) {
        return DailyStatsDto.builder()
                .date(dailyStats.getDate())
                .count(dailyStats.getClickCount())
                .build();
    }
} 