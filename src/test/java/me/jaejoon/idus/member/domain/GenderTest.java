package me.jaejoon.idus.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */
class GenderTest {

    @Test
    @DisplayName("key 값을 받은 경우 해당 타입의 enum 으로 반환해야함(남성)")
    void genderFromJsonMethod() {
        //given
        String key = "남성";
        //when
        Gender gender = Gender.fromJson(key);
        //then
        assertThat(gender).isEqualTo(Gender.MALE);
    }

    @Test
    @DisplayName("key 값을 받은 경우 해당 타입의 enum 으로 반환해야함(여성)")
    void genderFromJsonMethod2() {
        //given
        String key = "여성";
        //when
        Gender gender = Gender.fromJson(key);
        //then
        assertThat(gender).isEqualTo(Gender.FEMALE);
    }

    @Test
    @DisplayName("해당 key 값이 유효하지 않은 경우 (비공개)")
    void genderFromJsonMethod3() {
        //given
        String key = "";
        //when
        Gender gender = Gender.fromJson(key);
        //then
        assertThat(gender).isEqualTo(Gender.NONE);
    }
}