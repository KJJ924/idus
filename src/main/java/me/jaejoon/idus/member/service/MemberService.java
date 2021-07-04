package me.jaejoon.idus.member.service;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.dto.request.RequestSaveMember;
import me.jaejoon.idus.member.dto.response.ResponseMember;
import me.jaejoon.idus.member.exception.DuplicateEmailException;
import me.jaejoon.idus.member.exception.DuplicateNickname;
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

    public ResponseMember save(RequestSaveMember requestSaveMember) {

        memberSaveValidationCheck(requestSaveMember);
        String encodePW = passwordEncoder.encode(requestSaveMember.getPassword());

        Member member = requestSaveMember.toEntity();
        member.updatePassword(encodePW);
        memberRepository.save(member);

        return ResponseMember.toMapper(member);
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
