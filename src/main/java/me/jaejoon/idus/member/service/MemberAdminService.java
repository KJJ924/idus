package me.jaejoon.idus.member.service;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.dto.request.RequestMemberSearch;
import me.jaejoon.idus.member.dto.response.ResponseMember;
import me.jaejoon.idus.member.dto.response.ResponseMemberList;
import me.jaejoon.idus.member.dto.response.ResponseMembersPaging;
import me.jaejoon.idus.member.exception.NotFoundMemberException;
import me.jaejoon.idus.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
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
}
