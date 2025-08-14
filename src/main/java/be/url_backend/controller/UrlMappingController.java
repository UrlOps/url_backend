package be.url_backend.controller;

import be.url_backend.common.ResponseText;
import be.url_backend.domain.UrlMapping;
import be.url_backend.dto.request.UrlCreateRequestDto;
import be.url_backend.dto.response.UrlResponseDto;
import be.url_backend.dto.common.ApiResponse;
import be.url_backend.service.UrlMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/urls")
public class UrlMappingController {

    private final UrlMappingService urlMappingService;

    private final String BASE_URL = "http://prul.kr/";

    /**
     * 단축 URL 생성 API
     *
     * @param request 단축할 원본 URL 요청 데이터
     * @return 생성된 단축 URL 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UrlResponseDto>> createShortUrl(@RequestBody UrlCreateRequestDto request) {
        UrlResponseDto urlResponseDto = urlMappingService.createShortUrl(request, BASE_URL);
        ApiResponse<UrlResponseDto> response = ApiResponse.<UrlResponseDto>builder()
                .msg(ResponseText.URL_CREATE_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.CREATED.value()))
                .data(urlResponseDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 모든 URL 조회 API
     *
     * @return 모든 URL 정보 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UrlResponseDto>>> getAllUrls() {
        List<UrlMapping> urlMappings = urlMappingService.getAllUrls();
        List<UrlResponseDto> responses = urlMappings.stream()
                .map(urlMapping -> UrlResponseDto.from(urlMapping, BASE_URL))
                .collect(Collectors.toList());
        ApiResponse<List<UrlResponseDto>> response = ApiResponse.<List<UrlResponseDto>>builder()
                .msg(ResponseText.URL_LIST_FETCH_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(responses)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 단축 URL 정보 조회 API
     *
     * @param shortKey 조회할 단축 URL의 shortKey
     * @return 원본 URL 정보
     */
    @GetMapping("/{shortKey}")
    public ResponseEntity<ApiResponse<UrlResponseDto>> getUrlInfo(@PathVariable String shortKey) {
        UrlResponseDto urlResponseDto = urlMappingService.getOriginalUrl(shortKey, BASE_URL);
        ApiResponse<UrlResponseDto> response = ApiResponse.<UrlResponseDto>builder()
                .msg(ResponseText.URL_INFO_FETCH_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(urlResponseDto)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * URL 삭제 API
     *
     * @param shortKey 삭제할 단축 URL의 shortKey
     */
    @DeleteMapping("/{shortKey}")
    public ResponseEntity<ApiResponse<Void>> deleteUrl(@PathVariable String shortKey) {
        urlMappingService.deleteUrl(shortKey);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .msg(ResponseText.URL_DELETE_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
