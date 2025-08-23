package be.url_backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // User / Admin
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 아이디입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 관리자를 찾을 수 없습니다."),

    // UrlMapping
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 URL을 찾을 수 없습니다."),
    URL_IS_ALREADY_SHORT(HttpStatus.BAD_REQUEST, "이미 충분히 짧은 URL이므로 단축할 수 없습니다."),
    INVALID_URL_FORMAT(HttpStatus.BAD_REQUEST, "올바른 URL 형식이 아닙니다. http:// 또는 https://로 시작하는 URL을 입력해주세요.");

    private final HttpStatus status;
    private final String message;
} 