package me.jaejoon.idus.order.api;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.order.dto.response.ResponseOrderList;
import me.jaejoon.idus.order.service.OrderAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/07
 */


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class OrderAdminApi {

    private final OrderAdminService orderAdminService;

    @GetMapping("/{userEmail}")
    public ResponseEntity<ResponseOrderList> searchMemberOrderList(@PathVariable String userEmail) {
        return ResponseEntity.ok(orderAdminService.getMemberOrderList(userEmail));
    }
}
