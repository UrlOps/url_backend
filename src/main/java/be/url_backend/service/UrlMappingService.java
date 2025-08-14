package be.url_backend.service;

import be.url_backend.domain.ClickLog;
import be.url_backend.domain.UrlMapping;
import be.url_backend.dto.request.UrlCreateRequestDto;
import be.url_backend.dto.response.UrlResponseDto;
import be.url_backend.repository.ClickLogRepository;
import be.url_backend.repository.UrlMappingRepository;
import be.url_backend.util.Base62Utils;
import be.url_backend.exception.CustomException;
import be.url_backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;
    private final ClickLogRepository clickLogRepository;

    public UrlResponseDto createShortUrl(UrlCreateRequestDto request, String baseUrl) {
        String shortKey;
        do {
            shortKey = Base62Utils.generateShortKey();
        } while (urlMappingRepository.findByShortKey(shortKey).isPresent());

        UrlMapping urlMapping = UrlMapping.createUrlMapping(request.getOriginalUrl());
        urlMapping.updateShortKey(shortKey);
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);

        return UrlResponseDto.from(savedUrlMapping, baseUrl);
    }

    public UrlResponseDto getOriginalUrl(String shortKey, String baseUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new CustomException(ErrorCode.URL_NOT_FOUND));

        ClickLog clickLog = ClickLog.createClickLog(urlMapping, "user-agent-placeholder", "referer-placeholder");
        clickLogRepository.save(clickLog);

        return UrlResponseDto.from(urlMapping, baseUrl);
    }

    @Transactional(readOnly = true)
    public List<UrlMapping> getAllUrls() {
        return urlMappingRepository.findAll();
    }

    public void deleteUrl(String shortKey) {
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new CustomException(ErrorCode.URL_NOT_FOUND));
        urlMappingRepository.delete(urlMapping);
    }
}
