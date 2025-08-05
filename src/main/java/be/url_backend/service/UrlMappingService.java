package be.url_backend.service;

import be.url_backend.domain.UrlMapping;
import be.url_backend.dto.UrlCreateRequestDto;
import be.url_backend.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;

    @Transactional
    public UrlMapping createShortUrl(UrlCreateRequestDto request) {
        if (urlMappingRepository.findByShortKey(request.getCustomKey()).isPresent()) {
            throw new IllegalArgumentException("커스텀 키가 이미 존재합니다. 다른 키를 사용해주세요.");
        }
        
        UrlMapping urlMapping = UrlMapping.createUrlMapping(request.getOriginalUrl(), request.getCustomKey());

        return urlMappingRepository.save(urlMapping);
    }

    public UrlMapping getOriginalUrl(String shortKey) {
        return urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new IllegalArgumentException("주어진 키에서 URL을 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteUrl(String shortKey) {
        UrlMapping urlMapping = getOriginalUrl(shortKey);
        urlMappingRepository.delete(urlMapping);
    }

}
