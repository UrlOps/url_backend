package be.url_backend.feature.url;

import be.url_backend.feature.log.ClickLogService;
import be.url_backend.common.exception.CustomException;
import be.url_backend.common.exception.ErrorCode;
import be.url_backend.common.util.Base62Utils;
import be.url_backend.feature.url.dto.UrlCreateRequestDto;
import be.url_backend.feature.url.dto.UrlResponseDto;
import be.url_backend.feature.url.repository.UrlMappingRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final ClickLogService clickLogService;

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

        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            throw new CustomException(ErrorCode.INVALID_URL_FORMAT);
        }
        
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

        clickLogService.logClickAndupdateDailyStats(urlMapping, request.getHeader("User-Agent"), request.getRemoteAddr());

        return urlMapping.getOriginalUrl();
    }
}
