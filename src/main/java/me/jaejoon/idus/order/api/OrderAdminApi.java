package me.jaejoon.idus.order.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

@Api(tags = "주문 관련 API(관리자)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class OrderAdminApi {

    private final OrderAdminService orderAdminService;

    @ApiOperation("회원 주문목록 조회")
    @GetMapping("/{userEmail}")
    public ResponseEntity<ResponseOrderList> searchMemberOrderList(
        @ApiParam(value = "회원 이메일", example = "test@email.com", required = true)
        @PathVariable String userEmail) {
        ResponseOrderList memberOrderList = orderAdminService.getMemberOrderList(userEmail);
        return ResponseEntity.ok(memberOrderList);
    }
}
