package me.jaejoon.idus.member.repository;

import static me.jaejoon.idus.member.domain.QMember.member;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.member.dto.request.RequestMemberSearch;
import me.jaejoon.idus.member.dto.response.QResponseMemberList;
import me.jaejoon.idus.member.dto.response.ResponseMemberList;
import me.jaejoon.idus.order.domain.QOrder;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/07
 */

@Repository
@RequiredArgsConstructor
public class MemberSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public Page<ResponseMemberList> getMembersIncludingLastOrder(
        RequestMemberSearch search, Pageable pageable) {

        QOrder o = new QOrder("o");
        QOrder o2 = new QOrder("o2");
        QueryResults<ResponseMemberList> result = jpaQueryFactory
            .select(new QResponseMemberList(
                member.name,
                member.nickname,
                member.tel,
                member.email,
                member.gender,
                o.orderNumber,
                o.itemName,
                o.consumer,
                o.paymentDateTime))
            .from(member)
            .leftJoin(o).on(member.email.eq(o.consumer))
            .leftJoin(o2)
            .on(member.email.eq(o2.consumer).and(o.paymentDateTime.before(o2.paymentDateTime)))
            .where(
                o2.paymentDateTime.isNull(),
                eqMemberName(search.getName()),
                eqMemberEmail(search.getEmail())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression eqMemberName(String name) {
        if (Strings.isEmpty(name)) {
            return null;
        }
        return member.name.eq(name);
    }

    private BooleanExpression eqMemberEmail(String email) {
        if (Strings.isEmpty(email)) {
            return null;
        }
        return member.email.eq(email);
    }
}
