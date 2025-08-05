package be.url_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_key", nullable = false)
    private String shortKey;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private long clickCount;

    @Column(nullable = false)
    private long uniqueCount;

    public DailyStats(String shortKey, LocalDate date, long clickCount, long uniqueCount) {
        this.shortKey = shortKey;
        this.date = date;
        this.clickCount = clickCount;
        this.uniqueCount = uniqueCount;
    }
}
