package me.jaejoon.idus.order.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */
class RandomOrderNumberTest {

    @Test
    @DisplayName("주문번호를 생성했을때 12자리여야 한다.")
    void createOrderNumber() {
        //given
        RandomOrderNumber randomOrderNumber = new RandomOrderNumber();
        //when
        String orderNumber = randomOrderNumber.create("testSymbols");
        //then
        assertThat(orderNumber.length()).isEqualTo(12);
    }

    @Test
    @DisplayName("주문번호를 생성했을때 중복이 존재하면 안된다.")
    void createOrderNumber2() {
        //given
        RandomOrderNumber randomOrderNumber = new RandomOrderNumber();
        Set<String> set = new HashSet<>();
        //when
        for (int i = 0; i < 20000; i++) {
            set.add(randomOrderNumber.create(UUID.randomUUID().toString()));
        }
        //then
        assertThat(set.size()).isEqualTo(20000);
    }

    @Test
    @DisplayName("주문번호를 생성했을때 대문자여야 한다.")
    void createOrderNumber3() {
        //given
        RandomOrderNumber randomOrderNumber = new RandomOrderNumber();

        //when
        String orderNumber = randomOrderNumber.create("testSymbols");
        String removeNumber = orderNumber.replaceAll("[0-9]", "");
        char[] array = removeNumber.toCharArray();

        //then
        for (char c : array) {
            assertThat(Character.isUpperCase(c)).isTrue();
        }
    }
}