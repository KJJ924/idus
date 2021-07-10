package me.jaejoon.idus.config;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.JwtAuthenticationFilter;
import me.jaejoon.idus.auth.JwtAuthenticationProvider;
import me.jaejoon.idus.auth.exception.handler.CustomAccessDeniedHandler;
import me.jaejoon.idus.auth.exception.handler.CustomAuthenticationEntryPoint;
import me.jaejoon.idus.member.domain.code.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private static final String[] PUBLIC_URIS = {
        "/", "/h2-db/**", "/members/login", "/members/signup", "/swagger-ui.html",
        "/swagger-resources/**", "/configuration/ui", "/v2/api-docs", "/webjars/**"
    };

    private static final String[] ADMIN_URIS = {"/admin/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.httpBasic().disable()
            .formLogin().disable()
            .logout().disable();

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable();

        http.addFilterBefore(
            new JwtAuthenticationFilter(jwtAuthenticationProvider),
            UsernamePasswordAuthenticationFilter.class
        );

        http.authorizeRequests()
            .antMatchers(PUBLIC_URIS).permitAll()
            .antMatchers(ADMIN_URIS).hasAnyAuthority(Role.ADMIN.getValue())
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(customAccessDeniedHandler)
            .authenticationEntryPoint(customAuthenticationEntryPoint);
    }
}
