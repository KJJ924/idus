package me.jaejoon.idus.member.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestMemberLogin {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @Builder
    private RequestMemberLogin(
        @Email @NotEmpty String email, @NotEmpty String password) {
        this.email = email;
        this.password = password;
    }
}
