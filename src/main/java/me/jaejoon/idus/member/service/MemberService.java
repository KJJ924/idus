package me.jaejoon.idus.member.service;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.auth.service.JwtService;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.dto.request.RequestMemberLogin;
import me.jaejoon.idus.member.dto.request.RequestSaveMember;
import me.jaejoon.idus.member.dto.response.ResponseLoginToken;
import me.jaejoon.idus.member.dto.response.ResponseMember;
import me.jaejoon.idus.member.exception.DuplicateEmailException;
import me.jaejoon.idus.member.exception.DuplicateNickname;
import me.jaejoon.idus.member.exception.LoginFailedException;
import me.jaejoon.idus.member.exception.NotFoundMemberException;
import me.jaejoon.idus.member.repository.MemberRepository;
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

    public ResponseMember save(RequestSaveMember requestSaveMember) {
        memberSaveValidationCheck(requestSaveMember);
        String encodePW = passwordEncoder.encode(requestSaveMember.getPassword());

        Member member = requestSaveMember.toEntity();
        member.updatePassword(encodePW);
        memberRepository.save(member);

        return ResponseMember.toMapper(member);
    }

    public ResponseLoginToken login(RequestMemberLogin requestMemberLogin) {
        Member member = memberRepository.findByEmail(requestMemberLogin.getEmail())
            .orElseThrow(LoginFailedException::new);

        checkPassword(requestMemberLogin.getPassword(), member.getPassword());

        return new ResponseLoginToken(jwtService.encode(member));
    }

    public ResponseMember getMemberDetail(AuthUser authUser) {
        Member member = findByEmailMember(authUser.getEmail());
        return ResponseMember.toMapper(member);
    }

    private Member findByEmailMember(String email) {
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
