package com.example.careeix.utils.dto;

import com.example.careeix.utils.exception.ApiErrorResponse;
import com.example.careeix.utils.exception.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationErrorResponse<T> {

    private String code;
    private LocalDateTime timeStamp;
    private String message;

    public static <T> ApplicationErrorResponse<T> error(ApiErrorResponse e){
        return (ApplicationErrorResponse<T>) ApplicationErrorResponse.builder()
                .code(String.valueOf(e.getCode()))
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();
    }
}
