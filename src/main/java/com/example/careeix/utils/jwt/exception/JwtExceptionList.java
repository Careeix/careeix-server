package com.example.careeix.utils.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionList {
    NOTFOUNDJWT("J2001", HttpStatus.NOT_FOUND, "JWT 토큰이 비어있습니다."),
    EXPIRE_ACCESSTOKEN("J2002", HttpStatus.FORBIDDEN, "ACCESS-TOKEN이 틀렸습니다.");

    private final String CODE;
    private final HttpStatus httpStatus;
    private final String MESSAGE;

}
