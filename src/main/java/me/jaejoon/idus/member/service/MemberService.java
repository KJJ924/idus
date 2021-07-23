package me.jaejoon.idus.member.service;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.auth.service.JwtService;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.dto.request.RequestMemberLogin;
import me.jaejoon.idus.member.dto.request.RequestMemberSearch;
import me.jaejoon.idus.member.dto.request.RequestSaveMember;
import me.jaejoon.idus.member.dto.response.ResponseLoginToken;
import me.jaejoon.idus.member.dto.response.ResponseMember;
import me.jaejoon.idus.member.dto.response.ResponseMemberList;
import me.jaejoon.idus.member.dto.response.ResponseMembersPaging;
import me.jaejoon.idus.member.exception.DuplicateEmailException;
import me.jaejoon.idus.member.exception.DuplicateNickname;
import me.jaejoon.idus.member.exception.LoginFailedException;
import me.jaejoon.idus.member.exception.NotFoundMemberException;
import me.jaejoon.idus.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseMember signUp(RequestSaveMember requestSaveMember) {
        memberSaveValidationCheck(requestSaveMember);

        String encodePassword = passwordEncoder.encode(requestSaveMember.getPassword());
        Member member = requestSaveMember.toEntity(encodePassword);

        memberRepository.save(member);

        return ResponseMember.toMapper(member);
    }

    public ResponseLoginToken login(RequestMemberLogin requestMemberLogin) {
        Member member = memberRepository.findByEmail(requestMemberLogin.getEmail())
            .orElseThrow(LoginFailedException::new);

        checkPassword(requestMemberLogin.getPassword(), member.getPassword());

        return new ResponseLoginToken(jwtService.encode(member));
    }

    public ResponseMember getMyInfo(AuthUser authUser) {
        Member member = findMemberBy(authUser.getEmail());
        return ResponseMember.toMapper(member);
    }

    public ResponseMember getMemberInfo(String email) {
        Member member = findMemberBy(email);
        return ResponseMember.toMapper(member);
    }

    public ResponseMembersPaging getMemberListIncludingLastOrders(RequestMemberSearch search,
        Pageable pageable) {

        Page<ResponseMemberList> membersIncludingLastOrder = memberRepository
            .getMembersIncludingLastOrder(search, pageable);

        return ResponseMembersPaging.toMapper(membersIncludingLastOrder);
    }

    private Member findMemberBy(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(NotFoundMemberException::new);
    }

    private void checkPassword(String requestPassword, String referencePassword) {
        if (!passwordEncoder.matches(requestPassword, referencePassword)) {
            throw new LoginFailedException();
        }
    }

    private void memberSaveValidationCheck(RequestSaveMember requestSaveMember) {
        if (memberRepository.existsByEmail(requestSaveMember.getEmail())) {
            throw new DuplicateEmailException();
        }
        if (memberRepository.existsByNickname(requestSaveMember.getNickname())) {
            throw new DuplicateNickname();
        }
    }


}
