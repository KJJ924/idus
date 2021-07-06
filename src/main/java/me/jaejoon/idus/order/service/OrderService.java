package me.jaejoon.idus.order.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.order.domain.Order;
import me.jaejoon.idus.order.dto.request.RequestOrderSave;
import me.jaejoon.idus.order.dto.response.ResponseOrder;
import me.jaejoon.idus.order.repository.OrderRepository;
import me.jaejoon.idus.order.util.RandomOrderNumber;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RandomOrderNumber randomOrderNumber;

    public ResponseOrder save(RequestOrderSave requestOrderSave, AuthUser authUser) {
        String orderNumber = createOrderNumber(requestOrderSave);
        Order order = Order.builder()
            .orderNumber(orderNumber)
            .itemName(requestOrderSave.getItem())
            .memberEmail(authUser.getEmail())
            .build();

        orderRepository.save(order);

        return ResponseOrder.toMapper(order);
    }

    private String createOrderNumber(RequestOrderSave requestOrderSave) {
        String item = requestOrderSave.getItem();
        return randomOrderNumber.create(item + LocalDateTime.now());
    }
}
