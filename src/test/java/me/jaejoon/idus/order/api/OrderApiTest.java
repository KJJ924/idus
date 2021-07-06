package me.jaejoon.idus.order.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.member.authtesthelper.WithAuthUser;
import me.jaejoon.idus.order.dto.request.RequestOrderSave;
import me.jaejoon.idus.order.repository.OrderRepository;
import me.jaejoon.idus.order.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */
@SpringBootTest
@AutoConfigureMockMvc
class OrderApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @AfterEach
    void clean() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("주문생성(성공)")
    @WithAuthUser(email = "test@email.com")
    void orderSave() throws Exception {
        //given
        RequestOrderSave requestOrderSave = new RequestOrderSave("item");
        String content = objectMapper.writeValueAsString(requestOrderSave);

        //when
        ResultActions action = mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        //then
        action.andExpect(status().isCreated());
        action.andExpect(jsonPath("$..[ 'orderNumber' ]").exists());
        action.andExpect(jsonPath("$..[ 'item' ]").value(requestOrderSave.getItem()));
        action.andExpect(jsonPath("$..[ 'paymentDate' ]").exists());
        action.andExpect(jsonPath("$..[ 'orderer' ]").value("test@email.com"));

    }


    @Test
    @DisplayName("주문생성(실패) - 인증객체가 없는경우")
    void orderSave_fail() throws Exception {
        //given
        RequestOrderSave requestOrderSave = new RequestOrderSave("item");
        String content = objectMapper.writeValueAsString(requestOrderSave);

        //when
        ResultActions action = mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        //then
        action.andExpect(status().isUnauthorized());
        action.andExpect(jsonPath("$..[ 'message' ]")
            .value(ErrorCode.NOT_EXISTENCE_OR_INVALID_TOKEN.getMessage()));
        action.andExpect(jsonPath("$..[ 'status' ]").value(401));
    }

    @Test
    @DisplayName("내 주문목록 보기(성공)")
    @WithAuthUser(email = "test@email.com")
    void myOrders() throws Exception {
        //given
        AuthUser authUser = new AuthUser("test@email.com", "ROLE_USER");
        RequestOrderSave requestOrderSave = new RequestOrderSave("item");
        RequestOrderSave requestOrderSave1 = new RequestOrderSave("item2");
        orderService.save(requestOrderSave, authUser);
        orderService.save(requestOrderSave1, authUser);

        //when
        ResultActions action = mockMvc.perform(get("/orders"));

        //then
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$..[ 'totalOrdersCount' ]").value(2));
    }
}