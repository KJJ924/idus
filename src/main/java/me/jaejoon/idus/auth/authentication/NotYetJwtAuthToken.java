package me.jaejoon.idus.auth.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */
public class NotYetJwtAuthToken extends AbstractAuthenticationToken {

    private final String jwtToken;

    public NotYetJwtAuthToken(String token) {
        super(null);
        jwtToken = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return jwtToken;
    }
}
