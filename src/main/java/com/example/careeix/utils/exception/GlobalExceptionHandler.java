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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApplicationErrorResponse<ApiErrorResponse> methodArgumentNotValidException(
        MethodArgumentNotValidException e
    ){
        String errorCode = requireNonNull(e.getFieldError()).getDefaultMessage();
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(errorCode, "유효하지 않는 값입니다.");
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorCode, "@Valid");

        return ApplicationErrorResponse
                .error(exceptionResponse);
    }

    @ExceptionHandler(ApplicationException.class)
    public ApplicationErrorResponse<ApiErrorResponse> applicationException(ApplicationException e) {
        String errorCode = e.getErrorCode();
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(errorCode, e.getMessage());

        log.warn(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                errorCode,
                e.getMessage()
        );
        return ApplicationErrorResponse
                .error(exceptionResponse);

    }

    @ExceptionHandler(DataAccessException.class)
    public ApplicationErrorResponse<ApiErrorResponse> dataAccessException(DataAccessException e) {
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(INTERNAL_SERVER_ERROR_CODE, "데이터 연결 에러가 발생했습니다.");

        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage()
        );
        return ApplicationErrorResponse
                .error(exceptionResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ApplicationErrorResponse<ApiErrorResponse> runtimeException(RuntimeException e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage()
        );
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(INTERNAL_SERVER_ERROR_CODE, "런타임 에러가 발생했습니.");

        return ApplicationErrorResponse
                .error(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApplicationErrorResponse<ApiErrorResponse> httpMessageException(HttpMessageNotReadableException e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage()
        );
        ApiErrorResponse exceptionResponse = new ApiErrorResponse(INTERNAL_SERVER_ERROR_CODE, "값을 알맞게 모두 입력해주세요");

        return ApplicationErrorResponse
                .error(exceptionResponse);
    }


}
