package me.jaejoon.idus.member.exception;

import me.jaejoon.idus.error.exception.BusinessException;
import me.jaejoon.idus.error.message.ErrorCode;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */
public class LoginFailedException extends BusinessException {

    public LoginFailedException() {
        super(ErrorCode.LOGIN_FAILED);
    }
}
