package me.jaejoon.idus.auth.exception;

import lombok.Getter;
import me.jaejoon.idus.error.message.ErrorCode;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */
@Getter
public class NotExistenceOrInvalidTokenException extends RuntimeException {

    private final String message = ErrorCode.NOT_EXISTENCE_OR_INVALID_TOKEN.getMessage();

}
