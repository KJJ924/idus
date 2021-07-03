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
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestSaveMember {

    @NotEmpty
    @Pattern(regexp = "[가-힣a-zA-Z]+$", message = FieldValidErrorMessages.NAME)
    @Size(max = 20)
    private String name;

    @NotEmpty
    @Pattern(regexp = "[a-z]+$", message = FieldValidErrorMessages.NICK_NAME)
    @Size(max = 30)
    private String nickName;

    @Pattern(regexp = "^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[#?!@$ %^&*-]).{10,}",
        message = FieldValidErrorMessages.PASSWORD)
    private String password;

    @NotEmpty
    @Size(max = 20)
    @Pattern(regexp = "^[0-9]+$", message = FieldValidErrorMessages.TEL)
    private String tel;

    @NotEmpty
    @Email(message = FieldValidErrorMessages.EMAIL)
    @Size(max = 100)
    private String email;

    private Gender gender;

    public Member toEntity() {
        return Member.builder()
            .name(name)
            .nickName(nickName)
            .password(password)
            .tel(tel)
            .email(email)
            .gender(gender)
            .build();
    }
}
