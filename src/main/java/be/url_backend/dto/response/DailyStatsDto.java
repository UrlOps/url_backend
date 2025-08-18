package be.url_backend.dto.response;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class DailyStatsDto {
    private Long id;
    private LocalDate date;
    private long clickCount;

    public DailyStatsDto(Long id, String date, long clickCount) {
        this.id = id;
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.clickCount = clickCount;
    }
}
