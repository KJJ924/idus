package me.jaejoon.idus.order.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */

@Getter
@NoArgsConstructor
public class ResponseOrderList {

    private String orderer;
    private int totalOrdersCount;
    private List<ResponseOrder> orders;

    public ResponseOrderList(String orderer, List<ResponseOrder> orders) {
        this.orderer = orderer;
        this.totalOrdersCount = orders.size();
        this.orders = orders;
    }

}
