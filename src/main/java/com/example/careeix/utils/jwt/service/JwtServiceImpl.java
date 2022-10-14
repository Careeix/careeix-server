package com.example.careeix.utils.jwt.service;

import com.example.careeix.config.BaseException;
import com.example.careeix.utils.jwt.exception.ExpireAccessException;
import com.example.careeix.utils.jwt.exception.NotFoundJwtException;
import com.example.careeix.utils.jwt.secret.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.example.careeix.config.BaseResponseStatus.EMPTY_JWT;
import static com.example.careeix.config.BaseResponseStatus.INVALID_JWT;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtServiceImpl implements JwtService {


    /**
     * 엑세스 토큰 생성
     */

    @Override
    public String createJwt(long userIdx){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }



    /**
   Header에서 X-ACCESS-TOKEN 으로 JWT 추출
   @return String
    */
    @Override
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /**
    JWT에서 userId 추출
    @return Long
     */
    @Override
    public Long getUserId() {
        //1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0)
            throw new NotFoundJwtException();

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new ExpireAccessException();
        }

        // 3. userIdx 추출
        return claims.getBody().get("userIdx", Long.class);
    }

}