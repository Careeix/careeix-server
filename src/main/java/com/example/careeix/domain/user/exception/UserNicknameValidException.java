package com.example.careeix.domain.user.exception;

public class UserNicknameValidException extends UserException {
    public UserNicknameValidException() {
        super(UserExceptionList.USER_NICKNAME_VALID.getCODE(),
                UserExceptionList.USER_NICKNAME_VALID.getHttpStatus(),
                UserExceptionList.USER_NICKNAME_VALID.getMESSAGE()
        );
    }
}
