package com.example.careeix.utils.jwt.exception;

import com.example.careeix.utils.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class JwtException extends ApplicationException {

    protected JwtException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
