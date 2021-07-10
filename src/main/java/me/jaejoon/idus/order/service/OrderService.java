package me.jaejoon.idus.order.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.order.domain.Order;
import me.jaejoon.idus.order.dto.request.RequestOrderSave;
import me.jaejoon.idus.order.dto.response.ResponseOrder;
import me.jaejoon.idus.order.dto.response.ResponseOrderList;
import me.jaejoon.idus.order.repository.OrderRepository;
import me.jaejoon.idus.util.RandomOrderNumber;
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
        Order order = Order.builder()
            .orderNumber(randomOrderNumber.create())
            .itemName(requestOrderSave.getItem())
            .consumer(authUser.getEmail())
            .build();

        orderRepository.save(order);

        return ResponseOrder.toMapper(order);
    }

    public ResponseOrderList getOrderList(AuthUser authUser) {
        List<ResponseOrder> orders = orderRepository.findByConsumer(authUser.getEmail())
            .stream()
            .map(ResponseOrder::toMapper)
            .collect(Collectors.toList());
        return new ResponseOrderList(authUser.getEmail(), orders);
    }
}
