package com.example.careeix.utils.file.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileExceptionList {
    FILE_EXTENSION("F2001", HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다."),
    FILE_LOAD_FAILED("F2002", HttpStatus.BAD_REQUEST, "파일 불러오기에 실패했습니다."),
    FILE_SAVE_FAILED("F2003", HttpStatus.BAD_REQUEST, "파일 저장에 실패했습니다."),
    IMAGE_NOT_FOUND("F2004", HttpStatus.NOT_FOUND, "해당 이미지가 존재하지 않습니다.");

    private final String CODE;
    private final HttpStatus httpStatus;
    private final String MESSAGE;
}
