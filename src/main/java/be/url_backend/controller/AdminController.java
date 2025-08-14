package be.url_backend.controller;

import be.url_backend.dto.request.AdminLoginRequestDto;
import be.url_backend.dto.response.AdminResponseDto;
import be.url_backend.dto.request.AdminSignupRequestDto;
import be.url_backend.dto.response.ClickLogResponseDto;
import be.url_backend.dto.response.DailyStatsDto;
import be.url_backend.dto.response.JwtResponseDto;
import be.url_backend.dto.common.ApiResponse;
import be.url_backend.common.ResponseText;
import be.url_backend.service.AdminService;
import be.url_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import be.url_backend.domain.Admin;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    /**
     * 관리자 회원가입 API
     *
     * @param requestDto 관리자 회원가입 요청 데이터
     * @return 생성된 관리자 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AdminResponseDto>> signup(@RequestBody AdminSignupRequestDto requestDto) {
        AdminResponseDto adminResponseDto = adminService.signup(requestDto);
        ApiResponse<AdminResponseDto> response = ApiResponse.<AdminResponseDto>builder()
                .msg(ResponseText.ADMIN_SIGNUP_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.CREATED.value()))
                .data(adminResponseDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 관리자 로그인 API
     *
     * @param requestDto 관리자 로그인 요청 데이터
     * @return JWT 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(@RequestBody AdminLoginRequestDto requestDto) {
        adminService.login(requestDto.getUsername(), requestDto.getPassword());
        final String token = jwtUtil.generateToken(requestDto.getUsername());
        ApiResponse<JwtResponseDto> response = ApiResponse.<JwtResponseDto>builder()
                .msg(ResponseText.ADMIN_LOGIN_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(new JwtResponseDto(token))
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 일별 통계 조회 API
     *
     * @return 일별 통계 데이터 목록
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<List<DailyStatsDto>>> getDailyStats(@AuthenticationPrincipal Admin admin) {
        List<DailyStatsDto> stats = adminService.getDailyStats();
        ApiResponse<List<DailyStatsDto>> response = ApiResponse.<List<DailyStatsDto>>builder()
                .msg(ResponseText.URL_DAILY_STATS_FETCH_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(stats)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 전체 클릭 로그 조회 API (페이징)
     *
     * @param pageable 페이징 정보
     * @return 클릭 로그 데이터 페이지
     */
    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<Page<ClickLogResponseDto>>> getAllClickLogs(Pageable pageable, @AuthenticationPrincipal Admin admin) {
        Page<ClickLogResponseDto> clickLogs = adminService.getAllClickLogs(pageable);
        ApiResponse<Page<ClickLogResponseDto>> response = ApiResponse.<Page<ClickLogResponseDto>>builder()
                .msg("클릭 로그 조회 성공")
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(clickLogs)
                .build();
        return ResponseEntity.ok(response);
    }
} 