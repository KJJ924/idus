package me.jaejoon.idus.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaejoon.idus.error.message.FieldValidErrorMessages;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.domain.types.Gender;
import me.jaejoon.idus.member.domain.types.Role;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestSaveMember {

    @ApiModelProperty(value = "이름", required = true, example = "KJJ")
    @Size(max = 20)
    @Pattern(regexp = "[가-힣a-zA-Z]+$", message = FieldValidErrorMessages.NAME)
    private String name;


    @ApiModelProperty(value = "별명", required = true, example = "nickname")
    @Size(max = 30)
    @Pattern(regexp = "[a-z]+$", message = FieldValidErrorMessages.NICK_NAME)
    private String nickname;

    @ApiModelProperty(value = "비밀번호", required = true, example = "@Abc123456")
    @Size(min = 10)
    @Pattern(regexp = "^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[#?!@$ %^&*-]).{10,}",
        message = FieldValidErrorMessages.PASSWORD)
    private String password;

    @ApiModelProperty(value = "전화번호", required = true, example = "01012345678")
    @Size(max = 20)
    @Pattern(regexp = "^[0-9]+$", message = FieldValidErrorMessages.TEL)
    private String tel;


    @ApiModelProperty(value = "이메일", required = true, example = "test@email.com")
    @Size(max = 100)
    @NotEmpty
    @Email(message = FieldValidErrorMessages.EMAIL)
    private String email;

    @ApiModelProperty(value = "성별 남자,여자 (입력안할시 비공개)", example = "남자")
    @NotNull
    private Gender gender;

    @ApiModelProperty(value = "권한 ROLE_USER, ROLE_ADMIN (입력안할시 USER)", example = "ROLE_ADMIN")
    @NotNull
    private Role role;

    public Member toEntity(String encodingPassword) {
        return Member.builder()
            .name(name)
            .nickname(nickname)
            .password(encodingPassword)
            .tel(tel)
            .email(email)
            .gender(gender)
            .role(role)
            .build();
    }
}
