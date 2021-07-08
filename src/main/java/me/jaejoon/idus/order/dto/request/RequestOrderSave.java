package me.jaejoon.idus.order.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestOrderSave {

    @NotEmpty
    @ApiModelProperty(value = "물품 이름", required = true, example = "모자")
    private String item;

    public RequestOrderSave(@NotEmpty String item) {
        this.item = item;
    }
}
