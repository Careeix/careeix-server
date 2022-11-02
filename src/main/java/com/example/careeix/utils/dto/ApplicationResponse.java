package com.example.careeix.utils.dto;

import com.example.careeix.utils.exception.ApiErrorResponse;
import com.example.careeix.utils.exception.ApplicationException;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse<T> {

    private String code;
    private LocalDateTime timeStamp;
    private String message;
    private T data; // == body

    public static <T> ApplicationResponse<T> create(T data){
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .code(String.valueOf(HttpStatus.CREATED.value()))
                .timeStamp(LocalDateTime.now())
                .message("성공")
                .data(data)
                .build();
    }

    public static <T> ApplicationResponse<T> ok(){
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .data(null)
                .timeStamp(LocalDateTime.now())
                .message("성공")
                .build();
    }

    public static <T> ApplicationResponse<T> ok(T data){
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .data(data)
                .timeStamp(LocalDateTime.now())
                .message("성공")
                .build();
    }


    public static <T> ApplicationResponse<T> error(ApiErrorResponse e){
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .code(String.valueOf(e.getCode()))
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();
    }
}
