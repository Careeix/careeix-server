package com.example.careeix.domain.user.exception.oauth2.kakao;


import com.example.careeix.domain.user.exception.UserException;

public class KakaoFailException extends UserException {
    public KakaoFailException() {
        super(KakaoUserExceptionList.FAILED_KAKAO.getCODE(),
                KakaoUserExceptionList.FAILED_KAKAO.getHttpStatus(),
                KakaoUserExceptionList.FAILED_KAKAO.getMESSAGE()
        );
    }
}
