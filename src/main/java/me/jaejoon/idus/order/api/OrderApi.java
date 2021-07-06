package me.jaejoon.idus.order.api;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.order.dto.request.RequestOrderSave;
import me.jaejoon.idus.order.dto.response.ResponseOrder;
import me.jaejoon.idus.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderApi {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseOrder> orderSave(
        @Valid @RequestBody RequestOrderSave requestOrderSave,
        @AuthenticationPrincipal AuthUser authUser) {
        ResponseOrder responseOrder = orderService.save(requestOrderSave, authUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }
}
