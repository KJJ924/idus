package me.jaejoon.idus.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.auth.authentication.JwtAuthToken;
import me.jaejoon.idus.auth.authentication.NotYetJwtAuthToken;
import me.jaejoon.idus.auth.service.JwtService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        if (authentication == null || !supports(authentication.getClass())) {
            return null;
        }
        String token = (String) authentication.getPrincipal();

        try {
            AuthUser authUser = jwtService.decode(token);
            return new JwtAuthToken(authUser);
        } catch (JWTVerificationException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(NotYetJwtAuthToken.class);
    }
}
