package me.jaejoon.idus.error.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

@Getter
public enum ErrorCode {

    //Member
    DUPLICATION_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임 입니다."),
    DUPLICATION_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다.");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
