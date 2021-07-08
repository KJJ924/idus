package me.jaejoon.idus.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "이름", example = "KJJ")
    private String name;

    @ApiModelProperty(value = "이메일", example = "test@email.com")
    private String email;
}
