package me.jaejoon.idus.order.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.order.dto.request.RequestOrderSave;
import me.jaejoon.idus.order.dto.response.ResponseOrder;
import me.jaejoon.idus.order.dto.response.ResponseOrderList;
import me.jaejoon.idus.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */


@Api(tags = "주문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderApi {

    private final OrderService orderService;

    @ApiOperation("주문 생성")
    @PostMapping
    public ResponseEntity<ResponseOrder> orderSave(
        @ApiParam(value = "주문 요청", required = true) @Valid @RequestBody RequestOrderSave requestOrderSave,
        @AuthenticationPrincipal AuthUser authUser) {
        ResponseOrder responseOrder = orderService.save(requestOrderSave, authUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @ApiOperation("나의 주문 목록조회")
    @GetMapping
    public ResponseEntity<ResponseOrderList> myOrders(@AuthenticationPrincipal AuthUser authUser) {
        ResponseOrderList orderList = orderService.getOrderList(authUser);
        return ResponseEntity.ok(orderList);
    }
}
