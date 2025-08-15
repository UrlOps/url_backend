package be.url_backend.service;

import be.url_backend.domain.Admin;
import be.url_backend.dto.response.AdminResponseDto;
import be.url_backend.dto.request.AdminSignupRequestDto;
import be.url_backend.repository.AdminRepository;
import be.url_backend.repository.ClickLogRepository;
import be.url_backend.exception.CustomException;
import be.url_backend.exception.ErrorCode;
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

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClickLogRepository clickLogRepository;

    @Transactional
    public AdminResponseDto signup(AdminSignupRequestDto requestDto) {
        if (adminRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Admin admin = new Admin(requestDto.getUsername(), encodedPassword);
        Admin savedAdmin = adminRepository.save(admin);
        return AdminResponseDto.from(savedAdmin);
    }

    public void login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public Page<ClickLogResponseDto> getAllClickLogs(Pageable pageable) {
        return clickLogRepository.findAll(pageable).map(ClickLogResponseDto::from);
    }

    public List<DailyStatsDto> getDailyStats() {
        return clickLogRepository.findDailyClickStats();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByUsername(username)
                .map(admin -> new User(admin.getUsername(), admin.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
