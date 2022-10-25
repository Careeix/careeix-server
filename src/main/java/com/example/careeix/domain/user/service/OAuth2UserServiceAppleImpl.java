package com.example.careeix.domain.user.service;

import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.exception.oauth2.kakao.*;
import com.example.careeix.domain.user.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceAppleImpl implements OAuth2UserServiceApple {

    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUrl;
    /**
     * kakao login or sign up
     */

    @Override
    public User validateAppleAccessToken(String accessToken) {
        HashMap<String, Object> kakaoUserInfo = getAppleUserInfo(accessToken);
        return saveOrGetAppleUser(kakaoUserInfo);
    }

    private HashMap<String, Object> getAppleUserInfo(String accessToken) {

        HashMap<String, Object> resultMap = new HashMap<>();

        HttpURLConnection conn = null;
        try {
            URL url = new URL(kakaoUserInfoUrl);
            conn = (HttpURLConnection) url.openConnection();
            try {

                conn.setRequestMethod("GET");

                //요청에 필요한 Header에 포함될 내용
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String br_line = "";
                    String result = "";

                    while ((br_line = br.readLine()) != null) {
                        result += br_line;
                    }
                    System.out.println("response:" + result);


                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(result);

                    JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
                    JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

                    String id = element.getAsJsonObject().get("id").getAsString();
                    String email = kakao_account.getAsJsonObject().get("email").getAsString();

                    resultMap.put("id", id);
                    resultMap.put("email", email);

                    System.out.println("결과 : " + resultMap);
                    br.close();
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    throw new KakaoUnAuthorizedFaildException();
                } else {
                    throw new KakaoFailException();
                }
            }catch (IOException e) {
                throw new KakaoApiResponseException();
            }finally {
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            throw new KakaoUrlException(kakaoUserInfoUrl);
        } catch (ProtocolException e) {
            throw new KakaoProtocolException();
        } catch (IOException e) {
            throw new KakaoApiResponseException();
        }
        return resultMap;
    }

        private User saveOrGetAppleUser(HashMap<String, Object> kakaoUserInfo) {
        // 회원가입을 한 유저면 반환, 아니면 셋팅
        User user = userRepository.findBySocialId((String) kakaoUserInfo.get("id"))
                .orElse(User.toEntityOfKakaoUser(kakaoUserInfo));
        return user;
    }
}