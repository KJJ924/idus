package me.jaejoon.idus.member.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.member.dto.request.RequestMemberSearch;
import me.jaejoon.idus.member.dto.response.ResponseMember;
import me.jaejoon.idus.member.dto.response.ResponseMembersPaging;
import me.jaejoon.idus.member.service.MemberAdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */


@Api(tags = "회원 관련 API(관리자)")
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MemberAdminApi {

    private final MemberAdminService memberAdminService;

    @ApiOperation("회원상세 정보조회")
    @GetMapping("/{email}")
    public ResponseEntity<ResponseMember> getMemberDetail(
        @ApiParam(value = "회원 이메일", example = "test@email.com", required = true)
        @PathVariable String email) {
        return ResponseEntity.ok(memberAdminService.getMemberInfo(email));
    }

    @ApiOperation("회원리스트 조회(마지막 주문 포함)")
    @GetMapping
    public ResponseEntity<ResponseMembersPaging> getMemberList(
        RequestMemberSearch requestMemberSearch, @PageableDefault(size = 6) Pageable pageable) {

        ResponseMembersPaging response =
            memberAdminService.getMemberListIncludingLastOrders(requestMemberSearch, pageable);

        return ResponseEntity.ok(response);
    }
}
