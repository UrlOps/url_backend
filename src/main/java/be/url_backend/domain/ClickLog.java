package be.url_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClickLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_key", nullable = false)
    private String shortKey;

    @Column(nullable = false)
    private LocalDateTime clickedAt;

    @Column(length = 512)
    private String userAgent;
    
    @Column(length = 100)
    private String country;

    public ClickLog(String shortKey, LocalDateTime clickedAt, String userAgent, String country) {
        this.shortKey = shortKey;
        this.clickedAt = clickedAt;
        this.userAgent = userAgent;
        this.country = country;
    }
}
