package me.jaejoon.idus.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaejoon.idus.member.domain.Gender;
import me.jaejoon.idus.order.dto.response.ResponseOrder;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/07
 */

@Getter
@NoArgsConstructor
public class ResponseMemberList {

    private String name;

    private String nickname;

    private String tel;

    private String email;

    private String gender;

    private ResponseOrder lastOrder;

    @QueryProjection
    public ResponseMemberList(String name, String nickname, String tel, String email,
        Gender gender, String orderNumber, String item, String orderer, LocalDateTime paymentDate) {
        this.name = name;
        this.nickname = nickname;
        this.tel = tel;
        this.email = email;
        this.gender = gender.getName();
        this.lastOrder = new ResponseOrder(orderNumber, item, orderer, paymentDate);
    }
}
