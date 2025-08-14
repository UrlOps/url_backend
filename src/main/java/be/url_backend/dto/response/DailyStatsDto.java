package be.url_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyStatsDto {
    private Long id;
    private LocalDate date;
    private long clickCount;
}
