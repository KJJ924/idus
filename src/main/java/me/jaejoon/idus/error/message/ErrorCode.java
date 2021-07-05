package me.jaejoon.idus.error.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

@Getter
public enum ErrorCode {

    NOT_ALLOW_ARGUMENT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),

    //Auth
    NOT_EXISTENCE_OR_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "로그인을 하지않았거나 잘못된 접근입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "해당 요청에대한 권한이 존재하지 않습니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 맞지않습니다."),

    //Member
    DUPLICATION_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임 입니다."),
    DUPLICATION_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
