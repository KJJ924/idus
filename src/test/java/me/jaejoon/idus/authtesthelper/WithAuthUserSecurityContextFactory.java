package me.jaejoon.idus.authtesthelper;

import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.auth.authentication.JwtAuthToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */
public class WithAuthUserSecurityContextFactory implements
    WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        String email = annotation.email();
        String role = annotation.role();

        AuthUser authUser = new AuthUser(email, role);
        JwtAuthToken authentication = new JwtAuthToken(authUser);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        return context;
    }
}
