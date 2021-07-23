package me.jaejoon.idus.member.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

@Api(tags = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberAccountApi {

    private final MemberService memberService;

    @ApiOperation("회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<ResponseMember> signUp(
        @ApiParam(value = "회원 가입 요청", required = true)
        @Valid @RequestBody RequestSaveMember requestMember) {
        ResponseMember member = memberService.signUp(requestMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @ApiOperation("로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseLoginToken> login(
        @ApiParam(value = "로그인 요청", required = true)
        @Valid @RequestBody RequestMemberLogin requestMemberLogin) {
        ResponseLoginToken loginToken = memberService.login(requestMemberLogin);
        return ResponseEntity.ok(loginToken);
    }

    @ApiOperation("본인 상세정보 조회")
    @GetMapping("/personal-info")
    public ResponseEntity<ResponseMember> memberDetail(
        @AuthenticationPrincipal AuthUser authUser) {
        ResponseMember member = memberService.getMyInfo(authUser);
        return ResponseEntity.ok(member);
    }

}
