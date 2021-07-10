package me.jaejoon.idus.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import me.jaejoon.idus.auth.authentication.AuthUser;
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
 * @since 2021/07/06
 */


@SpringBootTest
class OrderServiceTest {

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
    void save() {
        //given
        RequestOrderSave requestOrderSave = new RequestOrderSave("item");
        AuthUser authUser = new AuthUser("test@email.com", "ROLE_USER");
        //when
        ResponseOrder order = orderService.save(requestOrderSave, authUser);

        //then
        assertThat(order.getOrderNumber()).isNotEmpty();
        assertThat(order.getOrderNumber().length()).isEqualTo(12);
        assertThat(order.getConsumer()).isEqualTo("test@email.com");
        assertThat(order.getItem()).isEqualTo("item");
    }


    @Test
    @DisplayName("나의 주문목록 보기(성공)")
    void myOrderList() {
        //given
        AuthUser authUser = new AuthUser("testUser@email.com", "ROLE_USER");
        RequestOrderSave requestOrderSave = new RequestOrderSave("testItem");
        RequestOrderSave requestOrderSave1 = new RequestOrderSave("testItem2");
        orderService.save(requestOrderSave, authUser);
        orderService.save(requestOrderSave1, authUser);
        //when
        ResponseOrderList orderList = orderService.getOrderList(authUser);
        List<String> itemList = orderList.getOrders().stream()
            .map(ResponseOrder::getItem)
            .collect(Collectors.toList());
        //then
        assertThat(orderList.getOrderer()).isEqualTo(authUser.getEmail());
        assertThat(orderList.getTotalOrdersCount()).isEqualTo(2);
        assertThat(itemList).contains("testItem", "testItem2");
    }
}