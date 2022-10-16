package com.example.careeix.domain.myfile.exception;

import com.example.careeix.utils.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class MyFileException extends ApplicationException {
    protected MyFileException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
