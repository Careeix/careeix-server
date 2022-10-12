package com.example.careeix.utils.file.exception;

public class FileLoadFailedException extends FileException {
    public FileLoadFailedException(){
        super(FileExceptionList.FILE_LOAD_FAILED.getCODE(),
                FileExceptionList.FILE_LOAD_FAILED.getHttpStatus(),
                FileExceptionList.FILE_LOAD_FAILED.getMESSAGE()
        );
    }
}
