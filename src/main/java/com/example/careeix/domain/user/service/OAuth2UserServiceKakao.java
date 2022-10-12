package com.example.careeix.domain.user.service;

import com.example.careeix.domain.user.dto.KakaoLoginRequest;
import com.example.careeix.domain.user.entity.User;

public interface OAuth2UserServiceKakao {
    User validateKakaoAccessToken(KakaoLoginRequest kakaoLoginRequest);
}
