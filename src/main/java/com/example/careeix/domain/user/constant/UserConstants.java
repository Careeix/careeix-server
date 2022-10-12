package com.example.careeix.domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserConstants {

    @Getter
    @AllArgsConstructor
    public enum eUser {

        eACTIVE("active"),
        eDELETE("DELETED");

        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum EOAuth2UserServiceImpl {

        eBearer("Bearer "),
        eAuthorization("Authorization"),
        eGetMethod("GET"),
        eResponse("response"),
        eNameAttribute("name"),
        eIdToken("id"),
        eEmailAttribute("email"),

        eKakaoProfileImageAttribute("profile_image"),
        eKakaoProperties("properties"),
        eKakaoAcount("kakao_account"),

        eGoogleTokenInvalid("잘못된 토큰입니다.");
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum ESocialProvider{
        eGoogle,
        eNaver,
        eKakao,
        eApple;
    }

}
