package com.example.careeix.domain.user.service;

import com.example.careeix.domain.user.dto.KakaoLoginRequest;
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

import static com.example.careeix.domain.user.constant.UserConstants.*;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceKakaoImpl implements OAuth2UserServiceKakao {

    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUrl;

    /**
     * kakao login or sign up
     */

    @Override
    public User validateKakaoAccessToken(KakaoLoginRequest kakaoLoginRequest) {
        HashMap<String, Object> kakaoUserInfo = getKakaoUserInfo(kakaoLoginRequest);
        return saveOrGetKakaoUser(kakaoUserInfo);
    }

    private HashMap<String, Object> getKakaoUserInfo(KakaoLoginRequest kakaoLoginRequest) {
        String accessToken = kakaoLoginRequest.getAccessToken();
        HashMap<String, Object> kakaoUserInfo = new HashMap<>();

        try {
            URL url = new URL(kakaoUserInfoUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            try {
                conn.setRequestMethod(EOAuth2UserServiceImpl.eGetMethod.getValue());
                conn.setRequestProperty(EOAuth2UserServiceImpl.eAuthorization.getValue(), EOAuth2UserServiceImpl.eBearer + accessToken);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = "";
                    String responseBody = "";

                    while ((line = br.readLine()) != null) {
                        responseBody += line;
                    }

                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(responseBody);

                    JsonObject properties = element.getAsJsonObject().get(EOAuth2UserServiceImpl.eKakaoProperties.getValue()).getAsJsonObject();
                    JsonObject kakaoAccount = element.getAsJsonObject().get(EOAuth2UserServiceImpl.eKakaoAcount.getValue()).getAsJsonObject();

                    String id = kakaoAccount.getAsJsonObject().get(EOAuth2UserServiceImpl.eIdToken.getValue()).getAsString();
                    String name = kakaoAccount.getAsJsonObject().get(EOAuth2UserServiceImpl.eNameAttribute.getValue()).getAsString();
                    String email = kakaoAccount.getAsJsonObject().get(EOAuth2UserServiceImpl.eEmailAttribute.getValue()).getAsString();
                    String profileImage = properties.getAsJsonObject().get(EOAuth2UserServiceImpl.eKakaoProfileImageAttribute.getValue()).getAsString();

                    kakaoUserInfo.put(EOAuth2UserServiceImpl.eIdToken.getValue(), id);
                    kakaoUserInfo.put(EOAuth2UserServiceImpl.eNameAttribute.getValue(), name);
                    kakaoUserInfo.put(EOAuth2UserServiceImpl.eEmailAttribute.getValue(), email);
                    kakaoUserInfo.put(EOAuth2UserServiceImpl.eKakaoProfileImageAttribute.getValue(), profileImage);

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
        return kakaoUserInfo;
    }

    private User saveOrGetKakaoUser(HashMap<String, Object> kakaoUserInfo) {
        User user = userRepository.findBySocialId(kakaoUserInfo.get(EOAuth2UserServiceImpl.eIdToken).toString())
                .orElse(User.toEntityOfKakaoUser(kakaoUserInfo));
        userRepository.save(user);
        return user;
    }
}
