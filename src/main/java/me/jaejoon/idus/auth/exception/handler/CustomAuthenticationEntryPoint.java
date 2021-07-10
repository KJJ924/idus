package me.jaejoon.idus.auth.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.error.response.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        String message = (String) request.getAttribute("exceptionMessage");
        setResponse(message, response);
    }

    private void setResponse(String message, HttpServletResponse response)
        throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(message)
            .status(HttpServletResponse.SC_UNAUTHORIZED)
            .build();

        String content = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().println(content);

    }
}
