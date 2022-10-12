package com.example.careeix.domain.user.exception.oauth2.kakao;

import com.example.careeix.domain.user.exception.UserException;

public class KakaoProtocolException extends UserException {
    public KakaoProtocolException() {
        super(KakaoUserExceptionList.FAILED_PROTOCOL_KAKAO.getCODE(),
                KakaoUserExceptionList.FAILED_PROTOCOL_KAKAO.getHttpStatus(),
                KakaoUserExceptionList.FAILED_PROTOCOL_KAKAO.getMESSAGE()
        );
    }
}
