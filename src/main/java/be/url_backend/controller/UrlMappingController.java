package be.url_backend.controller;

import be.url_backend.common.ResponseText;
import be.url_backend.dto.request.UrlCreateRequestDto;
import be.url_backend.dto.response.UrlResponseDto;
import be.url_backend.dto.common.ApiResponse;
import be.url_backend.dto.response.ClickLogResponseDto;
import be.url_backend.service.ClickLogService;
import be.url_backend.service.UrlMappingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UrlMappingController {

    private final UrlMappingService urlMappingService;
    private final ClickLogService clickLogService;

    /**
     * 단축 URL 생성 API
     *
     * @param request 단축할 원본 URL 요청 데이터
     * @return 생성된 단축 URL 정보
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
     * 모든 URL 조회 API
     *
     * @return 모든 URL 정보 목록
     */
    @GetMapping("/api/urls")
    public ResponseEntity<ApiResponse<List<UrlResponseDto>>> getAllUrls(HttpServletRequest httpServletRequest) {
        String baseUrl = getBaseUrl(httpServletRequest);
        List<UrlResponseDto> responses = urlMappingService.getAllUrls(baseUrl);
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
    @GetMapping("/api/urls/{shortKey}")
    public ResponseEntity<ApiResponse<UrlResponseDto>> getUrlInfo(
            @PathVariable String shortKey, HttpServletRequest httpServletRequest) {
        UrlResponseDto urlResponseDto = urlMappingService.getOriginalUrl(shortKey, getBaseUrl(httpServletRequest));
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
    @DeleteMapping("/api/urls/{shortKey}")
    public ResponseEntity<ApiResponse<Void>> deleteUrl(@PathVariable String shortKey) {
        urlMappingService.deleteUrl(shortKey);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .msg(ResponseText.URL_DELETE_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * 복합 인덱스 테스트용 API - 특정 IP의 특정 기간 클릭 로그 조회
     *
     * @param ipAddress 조회할 IP 주소
     * @param startDate 조회 시작일 (YYYY-MM-DD)
     * @param endDate   조회 종료일 (YYYY-MM-DD)
     * @return 해당 기간의 클릭 로그 목록
     */
    @GetMapping("/api/logs/{ipAddress}")
    public ResponseEntity<ApiResponse<List<ClickLogResponseDto>>> getLogsByIpForLoadTest(
            @PathVariable String ipAddress,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ClickLogResponseDto> logs = clickLogService.getClickLogsByIpForPeriod(ipAddress, startDate, endDate);
        ApiResponse<List<ClickLogResponseDto>> response = ApiResponse.<List<ClickLogResponseDto>>builder()
                .msg("복합 인덱스 테스트용 로그 조회 성공")
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(logs)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 단축 URL을 원본 URL로 리디렉션하고 클릭을 기록합니다.
     *
     * @param shortKey 리디렉션할 단축 URL의 shortKey
     * @param request  HTTP 요청 객체
     * @return 원본 URL로 리디렉션하는 ResponseEntity
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
