package com.example.careeix.domain.user.exception.oauth2.apple;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AppleUserExceptionList {
    FAILED_TO_VALIDATE_APPLE_LOGIN("A2001", HttpStatus.METHOD_NOT_ALLOWED ,"애플 로그인에 실패했습니다."),
    FAILED_TO_FIND_AVALIABLE_RSA("A2002", HttpStatus.INTERNAL_SERVER_ERROR,"유효한 RSA값을 찾지 못했습니다.");

    private final String CODE;
    private final HttpStatus httpStatus;
    private final String MESSAGE;
}
