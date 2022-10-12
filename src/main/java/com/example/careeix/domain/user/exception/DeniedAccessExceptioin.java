package com.example.careeix.domain.user.exception;

public class DeniedAccessExceptioin extends UserException {
    public DeniedAccessExceptioin() {
        super(UserExceptionList.DENIED_ACCESS.getCODE(),
                UserExceptionList.DENIED_ACCESS.getHttpStatus(),
                UserExceptionList.DENIED_ACCESS.getMESSAGE()
        );
    }
}

