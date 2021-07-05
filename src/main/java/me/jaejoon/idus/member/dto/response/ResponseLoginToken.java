package me.jaejoon.idus.member.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Getter
@NoArgsConstructor
public class ResponseLoginToken {

    private String token;

    public ResponseLoginToken(String token) {
        this.token = token;
    }
}
