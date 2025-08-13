package be.url_backend.service;

import be.url_backend.domain.UrlMapping;
import be.url_backend.dto.request.UrlCreateRequestDto;
import be.url_backend.dto.response.UrlResponseDto;
import be.url_backend.repository.UrlMappingRepository;
import be.url_backend.util.Base62Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;

    @Transactional
    public UrlResponseDto createShortUrl(UrlCreateRequestDto request, String baseUrl) {
        UrlMapping urlMapping;
        if (request.getCustomKey() != null && !request.getCustomKey().isEmpty()) {
            urlMappingRepository.findByShortKey(request.getCustomKey()).ifPresent(u -> {
                throw new IllegalArgumentException("커스텀 키가 이미 존재합니다. 다른 키를 사용해주세요.");
            });
            urlMapping = UrlMapping.createUrlMapping(request.getOriginalUrl());
            urlMapping.updateShortKey(request.getCustomKey());
        } else {
            urlMapping = UrlMapping.createUrlMapping(request.getOriginalUrl());
        }
        
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);

        if (request.getCustomKey() == null || request.getCustomKey().isEmpty()) {
            String shortKey = Base62Utils.encode(savedUrlMapping.getId());
            savedUrlMapping.updateShortKey(shortKey);
            urlMappingRepository.save(savedUrlMapping);
        }

        return UrlResponseDto.from(savedUrlMapping, baseUrl);
    }

    public UrlResponseDto getOriginalUrl(String shortKey, String baseUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new IllegalArgumentException("주어진 키에서 URL을 찾을 수 없습니다."));
        return UrlResponseDto.from(urlMapping, baseUrl);
    }

    public List<UrlMapping> getAllUrls() {
        return urlMappingRepository.findAll();
    }

    @Transactional
    public void deleteUrl(String shortKey) {
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new IllegalArgumentException("주어진 키에서 URL을 찾을 수 없습니다."));
        urlMappingRepository.delete(urlMapping);
    }

}
