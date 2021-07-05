package me.jaejoon.idus.auth.authentication;

import java.util.List;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Getter
public class JwtAuthToken extends AbstractAuthenticationToken {

    private final AuthUser authUser;

    public JwtAuthToken(AuthUser authUser) {
        super(List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.authUser = authUser;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return authUser;
    }
}
