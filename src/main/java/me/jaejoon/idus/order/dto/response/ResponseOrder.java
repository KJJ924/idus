package me.jaejoon.idus.order.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaejoon.idus.order.domain.Order;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseOrder {

    private String orderNumber;
    private String item;
    private String consumer;
    private LocalDateTime paymentDateTime;

    private ResponseOrder(Order order) {
        this.orderNumber = order.getOrderNumber();
        this.item = order.getItemName();
        this.consumer = order.getConsumer();
        this.paymentDateTime = order.getPaymentDateTime();
    }

    public static ResponseOrder toMapper(Order order) {
        return new ResponseOrder(order);
    }
}
