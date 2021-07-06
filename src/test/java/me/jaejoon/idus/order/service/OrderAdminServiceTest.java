package me.jaejoon.idus.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.member.domain.Gender;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.exception.NotFoundMemberException;
import me.jaejoon.idus.member.repository.MemberRepository;
import me.jaejoon.idus.order.dto.request.RequestOrderSave;
import me.jaejoon.idus.order.dto.response.ResponseOrder;
import me.jaejoon.idus.order.dto.response.ResponseOrderList;
import me.jaejoon.idus.order.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/07
 */

@SpringBootTest
class OrderAdminServiceTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderAdminService orderAdminService;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 주문목록 보기(성공)")
    void getMemberList() {
        //given
        String targetMember = "test@email.com";
        Member member = Member.builder()
            .password("password")
            .email(targetMember)
            .nickname("nickname")
            .name("Name")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        AuthUser authUser = new AuthUser(targetMember, "ROLE_USER");
        RequestOrderSave requestOrderSave = new RequestOrderSave("testItem");
        RequestOrderSave requestOrderSave1 = new RequestOrderSave("testItem2");
        memberRepository.save(member);
        orderService.save(requestOrderSave, authUser);
        orderService.save(requestOrderSave1, authUser);

        //when
        ResponseOrderList orderList = orderAdminService.getMemberOrderList(targetMember);
        List<String> itemList = orderList.getOrders().stream()
            .map(ResponseOrder::getItem)
            .collect(Collectors.toList());

        //then
        assertThat(orderList.getOrderer()).isEqualTo(authUser.getEmail());
        assertThat(orderList.getTotalOrdersCount()).isEqualTo(2);
        assertThat(itemList).contains("testItem", "testItem2");
    }

    @Test
    @DisplayName("회원 주문목록 보기(실패) - 해당회원이 존재하지않음")
    void getMemberList_fail() {
        //given
        String targetMember = "test@email.com";
        //when
        assertThatThrownBy(() -> orderAdminService.getMemberOrderList(targetMember))
            //then
            .isInstanceOf(NotFoundMemberException.class)
            .hasMessage(ErrorCode.NOT_FOUND_MEMBER.getMessage());
    }
}