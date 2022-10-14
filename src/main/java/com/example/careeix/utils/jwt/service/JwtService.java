package com.example.careeix.utils.jwt.service;

import javax.validation.Valid;

public interface JwtService {
    String createJwt(@Valid long UserId);
    String getJwt();
    Long getUserId();
}
