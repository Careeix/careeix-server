package com.example.careeix.utils.file.exception;

public class FileExtensionException extends FileException {
    public FileExtensionException(){
        super(FileExceptionList.FILE_EXTENSION.getCODE(),
                FileExceptionList.FILE_EXTENSION.getHttpStatus(),
                FileExceptionList.FILE_EXTENSION.getMESSAGE()
        );
    }
}
