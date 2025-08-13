package be.url_backend.service;

import be.url_backend.domain.Admin;
import be.url_backend.dto.response.AdminResponseDto;
import be.url_backend.dto.request.AdminSignupRequestDto;
import be.url_backend.repository.AdminRepository;
import be.url_backend.repository.ClickLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import be.url_backend.dto.response.ClickLogResponseDto;
import be.url_backend.dto.response.DailyStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClickLogRepository clickLogRepository;

    @Transactional
    public AdminResponseDto signup(AdminSignupRequestDto requestDto) {
        if (adminRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Admin admin = new Admin(requestDto.getUsername(), encodedPassword);
        Admin savedAdmin = adminRepository.save(admin);
        return AdminResponseDto.from(savedAdmin);
    }

    @Transactional(readOnly = true)
    public Admin login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return admin;
    }

    @Transactional(readOnly = true)
    public Page<ClickLogResponseDto> getAllClickLogs(Pageable pageable) {
        return clickLogRepository.findAll(pageable).map(ClickLogResponseDto::from);
    }

    @Transactional(readOnly = true)
    public List<DailyStatsDto> getDailyStats() {
        List<Object[]> results = clickLogRepository.findDailyClickStats();
        return results.stream()
                .map(result -> {
                    LocalDate date = ((Date) result[0]).toLocalDate();
                    Long count = (Long) result[1];
                    return new DailyStatsDto(date, count);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByUsername(username)
                .map(admin -> new User(admin.getUsername(), admin.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
} 