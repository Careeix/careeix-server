package com.example.careeix.domain.user.exception.oauth2.kakao;


import com.example.careeix.domain.user.exception.UserException;

public class KakaoUnAuthorizedFaildException extends UserException {
    public KakaoUnAuthorizedFaildException() {
        super(KakaoUserExceptionList.UNAUTHORIZED_KAKAO.getCODE(),
                KakaoUserExceptionList.UNAUTHORIZED_KAKAO.getHttpStatus(),
                KakaoUserExceptionList.UNAUTHORIZED_KAKAO.getMESSAGE()
        );
    }
}
