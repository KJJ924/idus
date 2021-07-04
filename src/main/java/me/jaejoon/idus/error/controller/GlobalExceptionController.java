package me.jaejoon.idus.error.controller;


import me.jaejoon.idus.error.exception.BusinessException;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.error.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

@ControllerAdvice
public class GlobalExceptionController {

    //비지니스 관련
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    // 데이터 바인딩 관련 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(
        MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NOT_ALLOW_ARGUMENT, e);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }


}
