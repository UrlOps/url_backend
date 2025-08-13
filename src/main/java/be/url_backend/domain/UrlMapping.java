package be.url_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_key", nullable = false, unique = true)
    private String shortKey;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false)
    private LocalDateTime expireAt;

    public static UrlMapping createUrlMapping(String originalUrl) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.originalUrl = originalUrl;
        urlMapping.expireAt = LocalDateTime.now().plusYears(1); // 기본 만료 기간 1년
        return urlMapping;
    }

    public void updateShortKey(String shortKey) {
        this.shortKey = shortKey;
    }
}
