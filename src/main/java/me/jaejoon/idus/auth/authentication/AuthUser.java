package me.jaejoon.idus.auth.authentication;

import lombok.Getter;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Getter
public class AuthUser {

    private final String email;

    public AuthUser(String email) {
        this.email = email;
    }
}
