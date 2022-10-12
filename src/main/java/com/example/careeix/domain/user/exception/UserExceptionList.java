package com.example.careeix.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionList {

    DENIED_ACCESS("U0001", HttpStatus.FORBIDDEN, "잘못된 접근입니다."),
    USER_NICKNAME_DUPLICATE("U0002", HttpStatus.CONFLICT, "해당 닉네임은 이미 존재하는 닉네임입니다."),
    USER_DELETED("U0003", HttpStatus.BAD_REQUEST, "해당 계정은 삭제된 계정입니다."),
    USER_EMAIL_DUPLICATE("U0004", HttpStatus.CONFLICT, "해당 이메일은 이미 존재하는 이메일입니다.");

    private final String CODE;
    private final HttpStatus httpStatus;
    private final String MESSAGE;

}
