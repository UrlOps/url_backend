package be.url_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_key", nullable = false, unique = true)
    private String shortKey;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expireAt;

    public UrlMapping(String shortKey, String originalUrl, LocalDateTime createdAt, LocalDateTime expireAt) {
        this.shortKey = shortKey;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
    }

    public static UrlMapping createUrlMapping(String originalUrl, String customKey) {
        String shortKey = (customKey != null && !customKey.isEmpty())
                ? customKey
                : generateRandomKey();
        
        return new UrlMapping(
                shortKey,
                originalUrl,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30)
        );
    }

    private static String generateRandomKey() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
