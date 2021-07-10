package me.jaejoon.idus.member.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import me.jaejoon.idus.error.message.FieldValidErrorMessages;
import me.jaejoon.idus.member.domain.code.Gender;
import me.jaejoon.idus.member.domain.code.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */
class RequestSaveMemberTest {

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("이름 필드에 한글, 영문 대소문자 외 값이 존재하는 경우")
    void fieldValidation() {

        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "@#!4^!", // ErrorPoint
                "aa",
                "@Kk12345678",
                "01000000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);

        //when
        Set<ConstraintViolation<RequestSaveMember>> violations = validator
            .validate(requestSaveMember);
        Optional<String> errorMsg = violations.stream().map(ConstraintViolation::getMessage)
            .findAny();

        //then
        assertThat(errorMsg).isNotEmpty();
        assertThat(errorMsg.get()).isEqualTo(FieldValidErrorMessages.NAME);
    }

    @Test
    @DisplayName("별명 필드에 영문 소문자 외 값이 존재하는 경우")
    void fieldValidation2() {

        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "KIMJAEJOON",
                "AAaa", // ErrorPoint
                "@Kk12345678",
                "01000000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);

        //when
        Set<ConstraintViolation<RequestSaveMember>> violations = validator
            .validate(requestSaveMember);
        Optional<String> errorMsg = violations.stream().map(ConstraintViolation::getMessage)
            .findAny();

        //then
        assertThat(errorMsg).isNotEmpty();
        assertThat(errorMsg.get()).isEqualTo(FieldValidErrorMessages.NICK_NAME);
    }


    @DisplayName("패스워드 필드에 영문 대문자, 영문 소문자, 특수 문자, 숫자 각 1개 이상씩 포함하지 않는 경우 ")
    @ParameterizedTest
    @ValueSource(strings = {"Kk2345678910", "@k2345678910", "@KK123456789", "@Abcdefghi"})
    void fieldValidation3(String password) {

        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "KIMJAEJOON",
                "aa",
                password,// ErrorPoint
                "01000000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);

        //when
        Set<ConstraintViolation<RequestSaveMember>> violations = validator
            .validate(requestSaveMember);
        Optional<String> errorMsg = violations.stream().map(ConstraintViolation::getMessage)
            .findAny();

        //then
        assertThat(errorMsg).isNotEmpty();
        assertThat(errorMsg.get()).isEqualTo(FieldValidErrorMessages.PASSWORD);
    }

    @DisplayName("전화번호 필드에 숫자 외 값이 존재하는 경우")
    @Test
    void fieldValidation4() {

        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "KIMJAEJOON",
                "aa",
                "@Abcdefgh012",
                "010000000A", // Error Point
                "test@email.com",
                Gender.NONE,
                Role.USER);

        //when
        Set<ConstraintViolation<RequestSaveMember>> violations = validator
            .validate(requestSaveMember);
        Optional<String> errorMsg = violations.stream().map(ConstraintViolation::getMessage)
            .findAny();

        //then
        assertThat(errorMsg).isNotEmpty();
        assertThat(errorMsg.get()).isEqualTo(FieldValidErrorMessages.TEL);
    }

    @DisplayName("이메일 형식에 맞지 않는 경우")
    @Test
    void fieldValidation5() {

        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "KIMJAEJOON",
                "aa",
                "@Abcdefgh012",
                "0100000000",
                "testemail.com",  // Error Point
                Gender.NONE,
                Role.USER);

        //when
        Set<ConstraintViolation<RequestSaveMember>> violations = validator
            .validate(requestSaveMember);
        Optional<String> errorMsg = violations.stream().map(ConstraintViolation::getMessage)
            .findAny();

        //then
        assertThat(errorMsg).isNotEmpty();
        assertThat(errorMsg.get()).isEqualTo(FieldValidErrorMessages.EMAIL);
    }
}