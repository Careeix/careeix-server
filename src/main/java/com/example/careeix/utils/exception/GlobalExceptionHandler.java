package com.example.careeix.utils.exception;

import com.example.careeix.utils.dto.ApplicationErrorResponse;
import com.example.careeix.utils.dto.ApplicationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT ="Class : {}, CODE : {}, Message : {}";
    private static final String INTERNAL_SERVER_ERROR_CODE = "S0001";
    private static final String BAD_REQUEST_ERROR_CODE = "S0002";
    ;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> methodArgumentNotValidException(
        MethodArgumentNotValidException e
    ){
        String errorCode = requireNonNull(e.getFieldError()).getDefaultMessage();
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(BAD_REQUEST_ERROR_CODE, errorCode);
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorCode, "@Valid");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> applicationException(ApplicationException e) {
        String errorCode = e.getErrorCode();
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(errorCode, e.getMessage());

        log.warn(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                errorCode,
                e.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);

    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiErrorResponse> dataAccessException(DataAccessException e) {
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(INTERNAL_SERVER_ERROR_CODE, "데이터 연결 에러가 발생했습니다.");

        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> runtimeException(RuntimeException e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage()
        );
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(INTERNAL_SERVER_ERROR_CODE, "런타임 에러가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> httpMessageException(HttpMessageNotReadableException e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage()
        );
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(INTERNAL_SERVER_ERROR_CODE, "값을 알맞게 모두 입력해주세요");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionResponse);
    }


}
