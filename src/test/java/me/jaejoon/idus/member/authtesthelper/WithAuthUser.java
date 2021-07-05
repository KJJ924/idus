package me.jaejoon.idus.member.authtesthelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthUserSecurityContextFactory.class)
public @interface WithAuthUser {

    String email();

    String role() default "ROLE_USER";
}
