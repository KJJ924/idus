package me.jaejoon.idus.member.repository;

import me.jaejoon.idus.member.dto.request.RequestMemberSearch;
import me.jaejoon.idus.member.dto.response.ResponseMemberList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/23
 */
public interface MemberCustomRepository {

    Page<ResponseMemberList> getMembersIncludingLastOrder(RequestMemberSearch search,
        Pageable pageable);
}
