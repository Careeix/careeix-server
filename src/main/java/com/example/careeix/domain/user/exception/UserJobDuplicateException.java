package com.example.careeix.domain.user.exception;

public class UserJobDuplicateException extends UserException {
    public UserJobDuplicateException() {
        super(UserExceptionList.USER_JOB_DUPLICATE.getCODE(),
                UserExceptionList.USER_JOB_DUPLICATE.getHttpStatus(),
                UserExceptionList.USER_JOB_DUPLICATE.getMESSAGE()
        );
    }
}
