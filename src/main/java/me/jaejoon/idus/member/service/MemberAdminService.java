package me.jaejoon.idus.member.service;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.dto.request.RequestMemberSearch;
import me.jaejoon.idus.member.dto.response.ResponseMember;
import me.jaejoon.idus.member.dto.response.ResponseMembersPaging;
import me.jaejoon.idus.member.exception.NotFoundMemberException;
import me.jaejoon.idus.member.repository.MemberRepository;
import me.jaejoon.idus.member.repository.MemberSearchRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Service
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberRepository memberRepository;
    private final MemberSearchRepository memberSearchRepository;


    public ResponseMember getMemberInfo(String email) {
        return ResponseMember.toMapper(findByEmailMember(email));
    }

    public ResponseMembersPaging getMemberListIncludingLastOrders(
        RequestMemberSearch search, Pageable pageable) {
        return ResponseMembersPaging
            .toMapper(memberSearchRepository.getMembersIncludingLastOrder(search, pageable));
    }

    private Member findByEmailMember(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(NotFoundMemberException::new);
    }


}
