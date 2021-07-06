package me.jaejoon.idus.order.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaejoon.idus.order.domain.Order;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseOrder {

    private String orderNumber;
    private String item;
    private String orderer;
    private LocalDateTime paymentDate;

    private ResponseOrder(Order order) {
        this.orderNumber = order.getOrderNumber();
        this.item = order.getItemName();
        this.orderer = order.getOrderer();
        this.paymentDate = order.getPaymentDate();
    }

    public static ResponseOrder toMapper(Order order) {
        return new ResponseOrder(order);
    }
}
