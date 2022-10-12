package com.example.careeix.utils.file.exception;

import com.example.careeix.utils.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FileException extends ApplicationException {
    protected FileException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
