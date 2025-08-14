package be.url_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClickLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_mapping_id", nullable = false)
    private UrlMapping urlMapping;

    @Column(nullable = false)
    private LocalDateTime clickedAt;

    @Column(length = 512)
    private String userAgent;
    
    @Column(length = 100)
    private String country;

    @Column
    private String referer;

    public static ClickLog createClickLog(UrlMapping urlMapping, String userAgent, String referer) {
        ClickLog clickLog = new ClickLog();
        clickLog.urlMapping = urlMapping;
        clickLog.userAgent = userAgent;
        clickLog.referer = referer;
        return clickLog;
    }
}
