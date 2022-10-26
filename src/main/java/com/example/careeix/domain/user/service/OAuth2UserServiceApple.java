package com.example.careeix.domain.user.service;

import com.example.careeix.domain.user.entity.User;

public interface OAuth2UserServiceApple {
    User validateAppleAccessToken(String accessToken);
}
