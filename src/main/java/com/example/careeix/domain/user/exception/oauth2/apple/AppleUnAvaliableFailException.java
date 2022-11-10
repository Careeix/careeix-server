package com.example.careeix.domain.user.exception.oauth2.apple;


import com.example.careeix.domain.user.exception.UserException;
import com.example.careeix.domain.user.exception.oauth2.kakao.KakaoUserExceptionList;

public class AppleUnAvaliableFailException extends UserException {
    public AppleUnAvaliableFailException() {
        super(AppleUserExceptionList.FAILED_TO_FIND_AVALIABLE_RSA.getCODE(),
                AppleUserExceptionList.FAILED_TO_FIND_AVALIABLE_RSA.getHttpStatus(),
                AppleUserExceptionList.FAILED_TO_FIND_AVALIABLE_RSA.getMESSAGE()
        );
    }
}
