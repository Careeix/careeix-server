package com.example.careeix.utils.dto;

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

    public static <T> ApplicationErrorResponse<T> error(ApplicationException e){
        return (ApplicationErrorResponse<T>) ApplicationErrorResponse.builder()
                .code(e.getErrorCode())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();
    }
}
