package com.example.careeix.utils.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@JsonPropertyOrder({"code", "timeStamp", "message"})
public class ApiErrorResponse {

    private String code;
    private LocalDateTime timeStamp;
    private String message;

    public ApiErrorResponse(String code, String message) {

        this.code = code;
        this.timeStamp = LocalDateTime.now().withNano(0);
        this.message = message;
    }
}
