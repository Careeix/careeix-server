package com.example.careeix.domain.myfile.exception;

public class NotFoundMyFileException extends MyFileException {
    public NotFoundMyFileException(){
        super(MyFileExceptionList.NOT_FOUND_MY_FILE.getCODE(),
                MyFileExceptionList.NOT_FOUND_MY_FILE.getHttpStatus(),
                MyFileExceptionList.NOT_FOUND_MY_FILE.getMESSAGE()
        );
    }
}
