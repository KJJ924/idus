package me.jaejoon.idus.order.dto.request;

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
    private String item;

    public RequestOrderSave(@NotEmpty String item) {
        this.item = item;
    }
}
