package me.jaejoon.idus.error.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import me.jaejoon.idus.error.message.ErrorCode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */


@Getter
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    private final int status;
    private final String message;
    private List<ErrorField> errors;


    private final LocalDateTime timeStamp = LocalDateTime.now();

    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(ErrorCode errorCode, MethodArgumentNotValidException e) {
        this.status = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
        this.errors = createMessage(e);
    }

    @Builder
    private ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private List<ErrorField> createMessage(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream().map(ErrorField::mapper).collect(Collectors.toList());
    }


    @Getter
    private static class ErrorField {

        private final String field;
        private final String value;
        private final String reason;

        private ErrorField(FieldError fieldError) {
            this.field = fieldError.getField();
            this.value = checkValue(fieldError.getRejectedValue());

            this.reason = fieldError.getDefaultMessage();
        }

        public static ErrorField mapper(FieldError fieldError) {
            return new ErrorField(fieldError);
        }

        public String checkValue(Object rejectedValue) {
            return (rejectedValue == null) ? null : rejectedValue.toString();
        }
    }
}

