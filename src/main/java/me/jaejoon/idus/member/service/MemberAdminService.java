package me.jaejoon.idus.member.service;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.dto.response.ResponseMember;
import me.jaejoon.idus.member.exception.NotFoundMemberException;
import me.jaejoon.idus.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Service
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberRepository memberRepository;


    public ResponseMember getMemberInfo(String email) {
        return ResponseMember.toMapper(findByEmailMember(email));
    }

    private Member findByEmailMember(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(NotFoundMemberException::new);
    }
}
