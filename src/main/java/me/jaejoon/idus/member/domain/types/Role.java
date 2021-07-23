package me.jaejoon.idus.member.domain.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */
@Getter
public enum Role {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Role fromJson(String role) {
        return Arrays.stream(Role.values())
            .filter(r -> r.value.equals(role))
            .findFirst()
            .orElse(Role.USER);
    }
}
