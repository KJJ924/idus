package me.jaejoon.idus.member.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaejoon.idus.member.domain.Member;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseMember {

    private String name;

    private String nickname;

    private String tel;

    private String email;

    private String gender;

    private ResponseMember(Member member) {
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.tel = member.getTel();
        this.email = member.getEmail();
        this.gender = member.getGender().getName();
    }

    public static ResponseMember toMapper(Member member) {
        return new ResponseMember(member);
    }

}
