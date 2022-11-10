package com.example.careeix.domain.user.exception;

public class UserUpdateNicknameDuplicateException extends UserException {
    public UserUpdateNicknameDuplicateException() {
        super(UserExceptionList.USER_UPDATE_NICKNAME_DUPLICATE.getCODE(),
                UserExceptionList.USER_UPDATE_NICKNAME_DUPLICATE.getHttpStatus(),
                UserExceptionList.USER_UPDATE_NICKNAME_DUPLICATE.getMESSAGE()
        );
    }
}
