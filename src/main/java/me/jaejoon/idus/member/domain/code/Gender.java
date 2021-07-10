package me.jaejoon.idus.member.domain.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    NONE("비공개");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    @JsonCreator
    public static Gender fromJson(String key) {
        return Arrays.stream(Gender.values())
            .filter(g -> g.name.equals(key))
            .findFirst()
            .orElse(Gender.NONE);
    }
}
