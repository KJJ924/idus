package me.jaejoon.idus.order.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.authtesthelper.WithAuthUser;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.member.domain.Gender;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.repository.MemberRepository;
import me.jaejoon.idus.order.dto.request.RequestOrderSave;
import me.jaejoon.idus.order.repository.OrderRepository;
import me.jaejoon.idus.order.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/07
 */

@SpringBootTest
@AutoConfigureMockMvc
class OrderAdminApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
        orderRepository.deleteAll();
    }


    @Test
    @DisplayName("회원 주문목록 보기(성공)")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void myOrders() throws Exception {

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
        ResultActions action = mockMvc.perform(get("/admin/orders/" + targetMember));

        //then
        String ordersInItemName = "$..orders[?(@.item == '%s')]";

        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$..[ 'totalOrdersCount' ]").value(2));
        action.andExpect(jsonPath(ordersInItemName, "testItem").exists());
        action.andExpect(jsonPath(ordersInItemName, "testItem2").exists());
    }

    @Test
    @DisplayName("회원 주문목록 보기(실패) - 해당하는 회원이 존재하지않음")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void myOrders_fail() throws Exception {

        //given
        String nonExistentMember = "test@email.com";

        //when
        ResultActions action = mockMvc.perform(get("/admin/orders/" + nonExistentMember));

        //then
        action.andExpect(status().isNotFound());
        action.andExpect(jsonPath("$..[ 'message' ]")
            .value(ErrorCode.NOT_FOUND_MEMBER.getMessage()));
        action.andExpect(jsonPath("$..[ 'status' ]").value(404));
    }

    @Test
    @DisplayName("회원 주문목록 보기(실패) - 접근 권한이 없음")
    @WithAuthUser(email = "admin@email.com")
    void myOrders_fail2() throws Exception {

        //given
        String targetMember = "test@email.com";

        //when
        ResultActions action = mockMvc.perform(get("/admin/orders/" + targetMember));

        //then
        action.andExpect(status().isForbidden());
        action.andExpect(jsonPath("$..[ 'message' ]").value(ErrorCode.FORBIDDEN.getMessage()));
        action.andExpect(jsonPath("$..[ 'status' ]").value(403));
    }

    @Test
    @DisplayName("회원 주문목록 보기(실패) - 인증(로그인하지않음)")
    void myOrders_fail3() throws Exception {

        //given
        String targetMember = "test@email.com";

        //when
        ResultActions action = mockMvc.perform(get("/admin/orders/" + targetMember));

        //then
        action.andExpect(status().isUnauthorized());
        action.andExpect(jsonPath("$..[ 'message' ]")
            .value(ErrorCode.NOT_EXISTENCE_OR_INVALID_TOKEN.getMessage()));
        action.andExpect(jsonPath("$..[ 'status' ]").value(401));
    }
}