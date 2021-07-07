package me.jaejoon.idus.member.repository;

import static me.jaejoon.idus.member.domain.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.member.dto.response.QResponseMemberList;
import me.jaejoon.idus.member.dto.response.ResponseMemberList;
import me.jaejoon.idus.order.domain.QOrder;
import org.springframework.stereotype.Repository;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/07
 */

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;


    //fixme  페이징 + 검색기능 추가
    public List<ResponseMemberList> getMemberListIncludingLastOrder() {
        QOrder o = new QOrder("o");
        QOrder o2 = new QOrder("o2");
        return jpaQueryFactory.select(new QResponseMemberList(
            member.name,
            member.nickname,
            member.tel,
            member.email,
            member.gender,
            o.orderNumber,
            o.itemName,
            o.orderer,
            o.paymentDate))
            .from(member)
            .leftJoin(o).on(member.email.eq(o.orderer))
            .leftJoin(o2).on(member.email.eq(o2.orderer).and(o.paymentDate.before(o2.paymentDate)))
            .where(o2.paymentDate.isNull())
            .fetch();
    }
}
