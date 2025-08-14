package be.url_backend.dto.response;

import be.url_backend.domain.DailyStats;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyStatsDto {
    private LocalDate date;
    private Long count;

    public DailyStatsDto(java.sql.Date date, Long count) {
        this.date = date.toLocalDate();
        this.count = count;
    }

    public static DailyStatsDto from(DailyStats dailyStats) {
        return DailyStatsDto.builder()
                .date(dailyStats.getDate())
                .count(dailyStats.getClickCount())
                .build();
    }
}
