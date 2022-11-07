package com.example.careeix.domain.user.exception.oauth2.apple;


import com.example.careeix.domain.user.exception.UserException;
import com.example.careeix.domain.user.exception.oauth2.kakao.KakaoUserExceptionList;

public class AppleFailException extends UserException {
    public AppleFailException() {
        super(AppleUserExceptionList.FAILED_TO_VALIDATE_APPLE_LOGIN.getCODE(),
                AppleUserExceptionList.FAILED_TO_VALIDATE_APPLE_LOGIN.getHttpStatus(),
                AppleUserExceptionList.FAILED_TO_VALIDATE_APPLE_LOGIN.getMESSAGE()
        );
    }
}
