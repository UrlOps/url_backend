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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_mapping_id", nullable = false)
    private UrlMapping urlMapping;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private long clickCount;

    public DailyStats(UrlMapping urlMapping, LocalDate date) {
        this.urlMapping = urlMapping;
        this.date = date;
        this.clickCount = 0;
    }

    public void incrementClickCount() {
        this.clickCount++;
    }
}
