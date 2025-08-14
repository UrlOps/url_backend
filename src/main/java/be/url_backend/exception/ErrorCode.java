package be.url_backend.exception;

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

    // UrlMapping
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, "주어진 키에서 URL을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
} 