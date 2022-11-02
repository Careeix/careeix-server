package com.example.careeix.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionList {

    DENIED_ACCESS("U4001", HttpStatus.FORBIDDEN, "잘못된 접근입니다."),
    USER_NICKNAME_DUPLICATE("U1001", HttpStatus.CONFLICT, "중복된 닉네임입니다."),

//    USER_LENGTH("U1006", HttpStatus.BAD_REQUEST, "범위를 체크해주세요"),
    USER_NICKNAME_VALID("U1007", HttpStatus.BAD_REQUEST, "유효하지 않은 닉네임 입니다."),


    USER_DELETED("U1002", HttpStatus.BAD_REQUEST, "해당 계정은 삭제된 계정입니다."),
    NOT_FOUND_USER("U1003", HttpStatus.BAD_REQUEST, "해당 아이디를 찾을 수 없습니다."),

    USER_DUPLICATE("U1004", HttpStatus.CONFLICT, "해당 유저는 이미 가입한 유저입니다."),
    USER_JOB_DUPLICATE("U1005", HttpStatus.CONFLICT, "중복된 세부직무가 있습니다.");


    private final String CODE;
    private final HttpStatus httpStatus;
    private final String MESSAGE;

}
