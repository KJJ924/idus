package me.jaejoon.idus.config;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.CustomAuthenticationEntryPoint;
import me.jaejoon.idus.auth.JwtAuthenticationFilter;
import me.jaejoon.idus.auth.JwtAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

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

        // FixMe Public Url 추가해야댐 !
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/").authenticated()
            .anyRequest().permitAll()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint);
    }
}
