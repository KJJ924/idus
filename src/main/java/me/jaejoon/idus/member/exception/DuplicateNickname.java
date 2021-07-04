package me.jaejoon.idus.member.exception;

import me.jaejoon.idus.error.exception.BusinessException;
import me.jaejoon.idus.error.message.ErrorCode;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

public class DuplicateNickname extends BusinessException {

    public DuplicateNickname() {
        super(ErrorCode.DUPLICATION_NICKNAME);
    }
}
