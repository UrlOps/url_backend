package be.url_backend.controller;

import be.url_backend.common.ResponseText;
import be.url_backend.dto.request.UrlCreateRequestDto;
import be.url_backend.dto.response.UrlResponseDto;
import be.url_backend.dto.common.ApiResponse;
import be.url_backend.service.UrlMappingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlMappingController {

    private final UrlMappingService urlMappingService;

    /**
     * 단축 URL 생성 API
     *
     * @param request            단축할 URL을 포함한 요청 DTO
     * @param httpServletRequest Base URL 생성을 위한 요청 객체
     * @return 생성된 단축 URL 정보를 담은 ApiResponse
     */
    @PostMapping("/api/urls")
    public ResponseEntity<ApiResponse<UrlResponseDto>> createShortUrl(
            @RequestBody UrlCreateRequestDto request, HttpServletRequest httpServletRequest) {
        UrlResponseDto urlResponseDto = urlMappingService.createShortUrl(request, getBaseUrl(httpServletRequest));
        ApiResponse<UrlResponseDto> response = ApiResponse.<UrlResponseDto>builder()
                .msg(ResponseText.URL_CREATE_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.CREATED.value()))
                .data(urlResponseDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 단축 URL 리디렉션 API
     * 단축 URL을 원본 URL로 리디렉션하고, 클릭 정보를 기록합니다.
     *
     * @param shortKey 리디렉션할 단축 URL 키
     * @param request  HTTP 요청 객체 (클릭 로그 기록에 사용)
     * @return 원본 URL로 리디렉션 응답
     */
    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @PathVariable String shortKey, HttpServletRequest request) {
        String originalUrl = urlMappingService.getOriginalUrlAndLogClick(shortKey, request);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }
}
