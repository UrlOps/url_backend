package be.url_backend.service;

import be.url_backend.domain.ClickLog;
import be.url_backend.domain.DailyStats;
import be.url_backend.domain.UrlMapping;
import be.url_backend.dto.request.UrlCreateRequestDto;
import be.url_backend.dto.response.UrlResponseDto;
import be.url_backend.exception.CustomException;
import be.url_backend.exception.ErrorCode;
import be.url_backend.repository.ClickLogRepository;
import be.url_backend.repository.DailyStatsRepository;
import be.url_backend.repository.UrlMappingRepository;
import be.url_backend.util.Base62Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final ClickLogRepository clickLogRepository;
    private final DailyStatsRepository dailyStatsRepository;

    /**
     * 단축 URL 생성
     *
     * @param requestDto 단축할 원본 URL 요청 데이터
     * @param baseUrl    단축 URL에 사용될 기본 URL
     * @return 생성된 단축 URL 정보
     */
    @Transactional
    public UrlResponseDto createShortUrl(UrlCreateRequestDto requestDto, String baseUrl) {
        String originalUrl = requestDto.getOriginalUrl();
        int shortenedUrlLength = baseUrl.length() + 1 + Base62Utils.SHORT_KEY_LENGTH;

        if (originalUrl.length() <= shortenedUrlLength) {
            throw new CustomException(ErrorCode.URL_IS_ALREADY_SHORT);
        }

        String shortKey;
        do {
            shortKey = Base62Utils.generateShortKey();
        } while (urlMappingRepository.findByShortKey(shortKey).isPresent());

        UrlMapping urlMapping = UrlMapping.createUrlMapping(requestDto.getOriginalUrl());
        urlMapping.updateShortKey(shortKey);
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);

        return UrlResponseDto.from(savedUrlMapping, baseUrl);
    }

    @Transactional
    public String getOriginalUrlAndLogClick(String shortKey, HttpServletRequest request) {
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new CustomException(ErrorCode.URL_NOT_FOUND));

        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();

        ClickLog clickLog = new ClickLog(urlMapping, userAgent, ipAddress);
        clickLogRepository.save(clickLog);

        updateDailyStats(urlMapping);

        return urlMapping.getOriginalUrl();
    }

    private void updateDailyStats(UrlMapping urlMapping) {
        LocalDate today = LocalDate.now();
        DailyStats dailyStats = dailyStatsRepository.findByUrlMappingAndDate(urlMapping, today)
                .orElse(new DailyStats(urlMapping, today));

        dailyStats.incrementClickCount();
        dailyStatsRepository.save(dailyStats);
    }

    /**
     * 모든 URL 조회
     */
    public List<UrlMapping> getAllUrls() {
        return urlMappingRepository.findAll();
    }

    /**
     * 단축 URL로 원본 URL 조회
     *
     * @param shortKey 조회할 단축 URL의 shortKey
     * @param baseUrl  단축 URL에 사용될 기본 URL
     * @return 원본 URL 정보
     */
    public UrlResponseDto getOriginalUrl(String shortKey, String baseUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new CustomException(ErrorCode.URL_NOT_FOUND));
        return UrlResponseDto.from(urlMapping, baseUrl);
    }

    /**
     * 단축 URL 삭제
     *
     * @param shortKey 삭제할 단축 URL의 shortKey
     */
    @Transactional
    public void deleteUrl(String shortKey) {
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new CustomException(ErrorCode.URL_NOT_FOUND));
        urlMappingRepository.delete(urlMapping);
    }
}
