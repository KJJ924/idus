package me.jaejoon.idus.error.exception;

import me.jaejoon.idus.error.message.ErrorCode;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
