package me.jaejoon.idus.auth;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jaejoon.idus.auth.authentication.NotYetJwtAuthToken;
import me.jaejoon.idus.auth.exception.NotExistenceOrInvalidTokenException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationProvider authenticationProvider;

    public JwtAuthenticationFilter(
        AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            String token = checkAuthorizationHeaderAndGetToken(authorizationHeader);

            NotYetJwtAuthToken notYetJwtAuthToken = new NotYetJwtAuthToken(token);
            Authentication authenticate = authenticationProvider.authenticate(notYetJwtAuthToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            filterChain.doFilter(request, response);

        } catch (NotExistenceOrInvalidTokenException | AuthenticationException e) {
            request.setAttribute("exceptionMessage", e.getMessage());
            filterChain.doFilter(request, response);
        }
    }


    private String checkAuthorizationHeaderAndGetToken(String authorizationHeader) {
        if (Strings.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotExistenceOrInvalidTokenException();
        }
        return authorizationHeader.substring(7);
    }
}
