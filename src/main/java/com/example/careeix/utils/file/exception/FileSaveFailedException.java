package com.example.careeix.utils.file.exception;

public class FileSaveFailedException extends FileException {
    public FileSaveFailedException(){
        super(FileExceptionList.FILE_SAVE_FAILED.getCODE(),
                FileExceptionList.FILE_SAVE_FAILED.getHttpStatus(),
                FileExceptionList.FILE_SAVE_FAILED.getMESSAGE()
        );
    }
}
