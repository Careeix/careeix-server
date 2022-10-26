package com.example.careeix.utils.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ApiErrorResponse {
    private boolean success;
    private LocalDateTime timeStamp;
    private String errorCode;
    private String message;

    public ApiErrorResponse(String errorCode, String message) {
        this.success = false;
        this.timeStamp = LocalDateTime.now().withNano(0);
        this.errorCode = errorCode;
        this.message = message;
    }
}
