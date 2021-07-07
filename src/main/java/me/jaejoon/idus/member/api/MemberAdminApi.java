package me.jaejoon.idus.member.api;

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


@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MemberAdminApi {

    private final MemberAdminService memberAdminService;

    @GetMapping("/{email}")
    public ResponseEntity<ResponseMember> getMemberDetail(@PathVariable String email) {
        return ResponseEntity.ok(memberAdminService.getMemberInfo(email));
    }

    @GetMapping
    public ResponseEntity<ResponseMembersPaging> getMemberList(
        RequestMemberSearch requestMemberSearch, @PageableDefault(size = 6) Pageable pageable) {

        ResponseMembersPaging response =
            memberAdminService.getMemberListIncludingLastOrders(requestMemberSearch, pageable);

        return ResponseEntity.ok(response);
    }
}
