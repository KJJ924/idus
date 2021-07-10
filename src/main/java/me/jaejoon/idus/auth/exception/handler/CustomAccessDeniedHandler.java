package me.jaejoon.idus.auth.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.error.response.ErrorResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(ErrorCode.FORBIDDEN.getMessage())
            .status(HttpServletResponse.SC_FORBIDDEN)
            .build();

        String content = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().println(content);
    }
}
