package com.example.careeix.utils.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ApiErrorResponse {
    private boolean success;
    private LocalDateTime timeStamp;
    private String code;
    private String message;

    public ApiErrorResponse(String code, String message) {
        this.success = false;
        this.timeStamp = LocalDateTime.now().withNano(0);
        this.code = code;
        this.message = message;
    }
}
