package me.jaejoon.idus.member.api;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.member.dto.request.RequestMemberLogin;
import me.jaejoon.idus.member.dto.request.RequestSaveMember;
import me.jaejoon.idus.member.dto.response.ResponseLoginToken;
import me.jaejoon.idus.member.dto.response.ResponseMember;
import me.jaejoon.idus.member.service.MemberService;
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
 * @since 2021/07/03
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberAccountApi {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseMember> save(
        @Valid @RequestBody RequestSaveMember requestMember) {
        ResponseMember member = memberService.save(requestMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginToken> login(
        @RequestBody @Valid RequestMemberLogin requestMemberLogin) {
        return ResponseEntity.ok(memberService.login(requestMemberLogin));
    }

    @GetMapping("/personal-info")
    public ResponseEntity<ResponseMember> memberDetail(
        @AuthenticationPrincipal AuthUser authUser) {
        ResponseMember member = memberService.getMemberDetail(authUser);
        return ResponseEntity.ok(member);
    }

}