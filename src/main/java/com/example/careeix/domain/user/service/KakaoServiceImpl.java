package com.example.careeix.domain.user.service;

import com.example.careeix.config.BaseException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Service
    public class KakaoServiceImpl {



        public String getKakaoAccessToken (String code) throws BaseException {
            String access_Token = "";
            String refresh_Token = "";
            String reqURL = "https://kauth.kakao.com/oauth/token";

            try {
                URL url = new URL(reqURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                //  HttpURLConnection 설정 값 셋팅
                //  GET 요청 - setDoOutput을 true
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                //  GET 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                StringBuilder sb = new StringBuilder();
                sb.append("grant_type=authorization_code");
                sb.append("&client_id=a082f3ed64125465bca264d048f69612"); // TODO REST_API_KEY 입력
                sb.append("&redirect_uri=http://localhost:9000/oauth/kakao"); // TODO 인가코드 받은 redirect_uri 입력

                sb.append("&code=" + code);
                bw.write(sb.toString());
                bw.flush();

//            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

                //  RETURN 값 result 변수에 저장
                //  요청을 통해 얻은 JSON 타입의 Response 메세지
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
            System.out.println("response body : " + result);

                //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);

                // 토큰 값 저장 및 리턴
                access_Token = element.getAsJsonObject().get("access_token").getAsString();
                refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

                System.out.println("access_token : " + access_Token);
                System.out.println("refresh_token : " + refresh_Token);

                br.close();
                bw.close();

            } catch (IOException e) {
            }

            return access_Token;
        }



    }

