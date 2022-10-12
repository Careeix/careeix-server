package com.example.careeix.utils.file.exception;

public class ImageNotFoundException extends FileException {
    public ImageNotFoundException(){
        super(FileExceptionList.IMAGE_NOT_FOUND.getCODE(),
                FileExceptionList.IMAGE_NOT_FOUND.getHttpStatus(),
                FileExceptionList.IMAGE_NOT_FOUND.getMESSAGE()
        );
    }
}
