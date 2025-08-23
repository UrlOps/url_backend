package be.url_backend.feature.admin;

import be.url_backend.feature.admin.dto.AdminResponseDto;
import be.url_backend.feature.admin.dto.AdminSignupRequestDto;
import be.url_backend.feature.stats.DailyStatsDto;
import be.url_backend.feature.log.repository.ClickLogRepository;
import be.url_backend.common.exception.CustomException;
import be.url_backend.common.exception.ErrorCode;
import be.url_backend.feature.stats.repository.DailyStatsRepository;
import be.url_backend.feature.url.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import be.url_backend.feature.log.ClickLogResponseDto;
import be.url_backend.feature.url.dto.UrlResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import be.url_backend.feature.url.UrlMapping;

import java.time.LocalDate;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClickLogRepository clickLogRepository;
    private final UrlMappingRepository urlMappingRepository;
    private final DailyStatsRepository dailyStatsRepository;

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

    public Page<UrlResponseDto> getAllUrlMappings(Pageable pageable, String shortKey, String originalUrl, LocalDate startDate, LocalDate endDate, String baseUrl) {
        Page<UrlResponseDto> page = urlMappingRepository.searchUrlMappings(pageable, shortKey, originalUrl, startDate, endDate);
        page.getContent().forEach(dto -> dto.setShortenUrl(baseUrl + "/" + dto.getShortKey()));
        return page;
    }

    public Page<ClickLogResponseDto> getAllClickLogs(Pageable pageable, String ipAddress, LocalDate startDate, LocalDate endDate) {
        return clickLogRepository.searchClickLogs(pageable, ipAddress, startDate, endDate);
    }

    public Page<DailyStatsDto> getDailyStats(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return dailyStatsRepository.searchDailyStats(pageable, startDate, endDate);
    }

    @Transactional
    public void deleteUrl(String shortKey) {
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new CustomException(ErrorCode.URL_NOT_FOUND));
        urlMappingRepository.delete(urlMapping);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByUsername(username)
                .map(admin -> new User(admin.getUsername(), admin.getPassword(), Collections
                        .singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
