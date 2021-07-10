package me.jaejoon.idus.member.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jaejoon.idus.authtesthelper.WithAuthUser;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.domain.code.Gender;
import me.jaejoon.idus.member.repository.MemberRepository;
import me.jaejoon.idus.order.domain.Order;
import me.jaejoon.idus.order.repository.OrderRepository;
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
 * @since 2021/07/05
 */

@SpringBootTest
@AutoConfigureMockMvc
class MemberAdminApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("회원정보 상세조회(관리자) - 성공")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void getMemberDetail() throws Exception {
        //given
        Member member = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("Name")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(member);
        //when
        ResultActions action = mockMvc.perform(get("/admin/members/test@test.com"));

        //then
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$..[ 'name' ]").value(member.getName()));
        action.andExpect(jsonPath("$..[ 'nickname' ]").value(member.getNickname()));
        action.andExpect(jsonPath("$..[ 'tel' ]").value(member.getTel()));
        action.andExpect(jsonPath("$..[ 'gender' ]").value(member.getGender().getName()));
        action.andExpect(jsonPath("$..[ 'email' ]").value(member.getEmail()));
    }

    @Test
    @DisplayName("회원정보 상세조회(관리자) - 실패(대상 없음)")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void getMemberDetail_fail() throws Exception {

        //when
        ResultActions action = mockMvc.perform(get("/admin/members/test@test.com"));

        //then
        action.andExpect(status().isNotFound());
        action
            .andExpect(jsonPath("$..[ 'message' ]").value(ErrorCode.NOT_FOUND_MEMBER.getMessage()));
        action.andExpect(jsonPath("$..[ 'status' ]").value(404));
    }

    @Test
    @DisplayName("회원정보 상세조회(관리자) - 실패(권한이 없음)")
    @WithAuthUser(email = "admin@email.com")
    void getMemberDetail_fail2() throws Exception {
        //given
        Member member = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("Name")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(member);

        //when
        ResultActions action = mockMvc.perform(get("/admin/members/test@test.com"));

        //then
        action.andExpect(status().isForbidden());
        action.andExpect(jsonPath("$..[ 'message' ]").value(ErrorCode.FORBIDDEN.getMessage()));
        action.andExpect(jsonPath("$..[ 'status' ]").value(403));
    }

    @Test
    @DisplayName("회원 리스트 보기(성공)")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void getMemberList() throws Exception {
        //given
        Member member = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("Name")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(member);

        Order order = Order.builder()
            .orderNumber("ABCD2FG12345")
            .itemName("item")
            .consumer("test@test.com")
            .build();

        orderRepository.save(order);

        //when
        ResultActions action = mockMvc.perform(get("/admin/members/"));

        //then
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$..[ 'name' ]").value(member.getName()));
        action.andExpect(jsonPath("$..[ 'nickname' ]").value(member.getNickname()));
        action.andExpect(jsonPath("$..[ 'tel' ]").value(member.getTel()));
        action.andExpect(jsonPath("$..[ 'gender' ]").value(member.getGender().getName()));
        action.andExpect(jsonPath("$..[ 'email' ]").value(member.getEmail()));
        action.andExpect(jsonPath("$..[ 'orderNumber' ]").value(order.getOrderNumber()));
        action.andExpect(jsonPath("$..[ 'item' ]").value(order.getItemName()));
        action.andExpect(jsonPath("$..[ 'consumer' ]").value(order.getConsumer()));
        action.andExpect(jsonPath("$..[ 'totalPage' ]").value(1));
        action.andExpect(jsonPath("$..[ 'elementsSize' ]").value(1));
        action.andExpect(jsonPath("$..[ 'pageSize' ]").value(6));
    }

    @Test
    @DisplayName("회원 리스트 보기(성공) - 이름통한 검색한다면 이름이 같은 대상만 조회해야한다.")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void getMemberList2() throws Exception {
        //given
        Member member = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("KJJ")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        Member member2 = Member.builder()
            .password("password")
            .email("test2@test.com")
            .nickname("nick")
            .name("test")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(member);
        memberRepository.save(member2);

        Order order = Order.builder()
            .orderNumber("ABCD2FG12345")
            .itemName("item")
            .consumer("test@test.com")
            .build();

        orderRepository.save(order);

        //when
        ResultActions action = mockMvc.perform(get("/admin/members?name=KJJ"));

        //then
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$..[ 'name' ]").value(member.getName()));
        action.andExpect(jsonPath("$..[ 'nickname' ]").value(member.getNickname()));
        action.andExpect(jsonPath("$..[ 'tel' ]").value(member.getTel()));
        action.andExpect(jsonPath("$..[ 'gender' ]").value(member.getGender().getName()));
        action.andExpect(jsonPath("$..[ 'email' ]").value(member.getEmail()));
        action.andExpect(jsonPath("$..[ 'orderNumber' ]").value(order.getOrderNumber()));
        action.andExpect(jsonPath("$..[ 'item' ]").value(order.getItemName()));
        action.andExpect(jsonPath("$..[ 'consumer' ]").value(order.getConsumer()));
        action.andExpect(jsonPath("$..[ 'totalPage' ]").value(1));
        action.andExpect(jsonPath("$..[ 'elementsSize' ]").value(1));
        action.andExpect(jsonPath("$..[ 'pageSize' ]").value(6));
    }

    @Test
    @DisplayName("회원 리스트 보기(성공) - 이메일통한 검색한다면 이메일이 같은 대상만 조회해야한다.")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void getMemberList3() throws Exception {
        //given
        Member member = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("KJJ")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        Member member2 = Member.builder()
            .password("password")
            .email("test2@test.com")
            .nickname("nick")
            .name("test")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(member);
        memberRepository.save(member2);

        Order order = Order.builder()
            .orderNumber("ABCD2FG12345")
            .itemName("item")
            .consumer("test@test.com")
            .build();

        orderRepository.save(order);

        //when
        ResultActions action = mockMvc.perform(get("/admin/members?email=test@test.com"));

        //then
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$..[ 'name' ]").value(member.getName()));
        action.andExpect(jsonPath("$..[ 'nickname' ]").value(member.getNickname()));
        action.andExpect(jsonPath("$..[ 'tel' ]").value(member.getTel()));
        action.andExpect(jsonPath("$..[ 'gender' ]").value(member.getGender().getName()));
        action.andExpect(jsonPath("$..[ 'email' ]").value(member.getEmail()));
        action.andExpect(jsonPath("$..[ 'orderNumber' ]").value(order.getOrderNumber()));
        action.andExpect(jsonPath("$..[ 'item' ]").value(order.getItemName()));
        action.andExpect(jsonPath("$..[ 'consumer' ]").value(order.getConsumer()));
        action.andExpect(jsonPath("$..[ 'totalPage' ]").value(1));
        action.andExpect(jsonPath("$..[ 'elementsSize' ]").value(1));
        action.andExpect(jsonPath("$..[ 'pageSize' ]").value(6));
    }

    @Test
    @DisplayName("회원 리스트 보기(성공) - 주문이 n개 이여도 항상 가장 최근 주문을 포함해야한다.")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void getMemberList4() throws Exception {
        //given
        Member member = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("KJJ")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(member);

        Order order = Order.builder()
            .orderNumber("ABCD2FG12345")
            .itemName("item")
            .consumer("test@test.com")
            .build();

        Order resentOrder = Order.builder()
            .orderNumber("FDA12342345")
            .itemName("idus")
            .consumer("test@test.com")
            .build();
        orderRepository.save(order);
        orderRepository.save(resentOrder);

        //when
        ResultActions action = mockMvc.perform(get("/admin/members"));

        //then
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$..[ 'name' ]").value(member.getName()));
        action.andExpect(jsonPath("$..[ 'nickname' ]").value(member.getNickname()));
        action.andExpect(jsonPath("$..[ 'tel' ]").value(member.getTel()));
        action.andExpect(jsonPath("$..[ 'gender' ]").value(member.getGender().getName()));
        action.andExpect(jsonPath("$..[ 'email' ]").value(member.getEmail()));
        action.andExpect(jsonPath("$..[ 'orderNumber' ]").value(resentOrder.getOrderNumber()));
        action.andExpect(jsonPath("$..[ 'item' ]").value(resentOrder.getItemName()));
        action.andExpect(jsonPath("$..[ 'consumer' ]").value(resentOrder.getConsumer()));
        action.andExpect(jsonPath("$..[ 'totalPage' ]").value(1));
        action.andExpect(jsonPath("$..[ 'elementsSize' ]").value(1));
        action.andExpect(jsonPath("$..[ 'pageSize' ]").value(6));
    }

    @Test
    @DisplayName("회원 리스트 보기(관리자) - 실패(권한이 없음)")
    @WithAuthUser(email = "admin@email.com")
    void getMemberList_fail() throws Exception {
        //given
        Member member = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("Name")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(member);

        //when
        ResultActions action = mockMvc.perform(get("/admin/members/"));

        //then
        action.andExpect(status().isForbidden());
        action.andExpect(jsonPath("$..[ 'message' ]").value(ErrorCode.FORBIDDEN.getMessage()));
        action.andExpect(jsonPath("$..[ 'status' ]").value(403));
    }
}