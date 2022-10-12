package com.example.careeix.domain.user.exception;

import com.example.careeix.utils.file.exception.FileException;
import org.springframework.http.HttpStatus;

public abstract class UserException extends FileException {

    protected UserException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
