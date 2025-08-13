package be.url_backend.controller;

import be.url_backend.dto.request.AdminLoginRequestDto;
import be.url_backend.dto.response.AdminResponseDto;
import be.url_backend.dto.request.AdminSignupRequestDto;
import be.url_backend.dto.response.ClickLogResponseDto;
import be.url_backend.dto.response.DailyStatsDto;
import be.url_backend.dto.response.JwtResponseDto;
import be.url_backend.dto.common.ApiResponse;
import be.url_backend.service.AdminService;
import be.url_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AdminResponseDto>> signup(@RequestBody AdminSignupRequestDto requestDto) {
        AdminResponseDto adminResponseDto = adminService.signup(requestDto);
        ApiResponse<AdminResponseDto> response = ApiResponse.<AdminResponseDto>builder()
                .msg("회원가입 성공")
                .statuscode(String.valueOf(HttpStatus.CREATED.value()))
                .data(adminResponseDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(@RequestBody AdminLoginRequestDto requestDto) {
        adminService.login(requestDto.getUsername(), requestDto.getPassword());
        final String token = jwtUtil.generateToken(requestDto.getUsername());
        ApiResponse<JwtResponseDto> response = ApiResponse.<JwtResponseDto>builder()
                .msg("로그인 성공")
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(new JwtResponseDto(token))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<List<DailyStatsDto>>> getDailyStats() {
        List<DailyStatsDto> stats = adminService.getDailyStats();
        ApiResponse<List<DailyStatsDto>> response = ApiResponse.<List<DailyStatsDto>>builder()
                .msg("일별 통계 조회 성공")
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(stats)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<Page<ClickLogResponseDto>>> getAllClickLogs(Pageable pageable) {
        Page<ClickLogResponseDto> clickLogs = adminService.getAllClickLogs(pageable);
        ApiResponse<Page<ClickLogResponseDto>> response = ApiResponse.<Page<ClickLogResponseDto>>builder()
                .msg("클릭 로그 조회 성공")
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(clickLogs)
                .build();
        return ResponseEntity.ok(response);
    }
} 