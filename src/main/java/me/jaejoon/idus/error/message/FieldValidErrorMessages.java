package me.jaejoon.idus.error.message;


/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */
public interface FieldValidErrorMessages {

    String NAME = "한글, 영문 대소문자만 허용합니다.";
    String NICK_NAME = "영문 소문자만 허용합니다.";
    String PASSWORD = "영문 대문자, 영문 소문자, 특수 문자, 숫자 각 1개 이상씩 포함해야 합니다.";
    String TEL = "숫자만 허용합니다.";
    String EMAIL = "이메일 형식이 아닙니다.";
}
