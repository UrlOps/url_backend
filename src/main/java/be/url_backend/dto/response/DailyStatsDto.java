package be.url_backend.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class DailyStatsDto {
    private String shortUrl;
    private LocalDate date;
    private Long count;
}
