package com.example.careeix.domain.user.exception;

public class UserDuplicateException extends UserException {
    public UserDuplicateException() {
        super(UserExceptionList.USER_DUPLICATE.getCODE(),
                UserExceptionList.USER_DUPLICATE.getHttpStatus(),
                UserExceptionList.USER_DUPLICATE.getMESSAGE()
        );
    }
}
