package me.jaejoon.idus.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/07
 */

@Getter
@Setter
@NoArgsConstructor
public class RequestMemberSearch {

    private String name;
    private String email;
}
