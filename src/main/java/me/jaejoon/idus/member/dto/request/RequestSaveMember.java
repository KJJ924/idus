package me.jaejoon.idus.member.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaejoon.idus.error.message.FieldValidErrorMessages;
import me.jaejoon.idus.member.domain.Gender;
import me.jaejoon.idus.member.domain.Member;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestSaveMember {


    @Size(max = 20)
    @Pattern(regexp = "[가-힣a-zA-Z]+$", message = FieldValidErrorMessages.NAME)
    private String name;


    @Size(max = 30)
    @Pattern(regexp = "[a-z]+$", message = FieldValidErrorMessages.NICK_NAME)
    private String nickname;

    @Size(min = 10)
    @Pattern(regexp = "^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[#?!@$ %^&*-]).{10,}",
        message = FieldValidErrorMessages.PASSWORD)
    private String password;

    @Size(max = 20)
    @Pattern(regexp = "^[0-9]+$", message = FieldValidErrorMessages.TEL)
    private String tel;

    @Size(max = 100)
    @NotEmpty
    @Email(message = FieldValidErrorMessages.EMAIL)
    private String email;

    private Gender gender;

    public Member toEntity() {
        return Member.builder()
            .name(name)
            .nickname(nickname)
            .password(password)
            .tel(tel)
            .email(email)
            .gender(gender)
            .build();
    }
}
