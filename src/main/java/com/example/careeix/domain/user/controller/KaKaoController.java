package com.example.careeix.domain.user.controller;

import com.example.careeix.config.BaseException;
import com.example.careeix.domain.user.dto.MessageResponse;
import com.example.careeix.domain.user.service.KakaoServiceImpl;
import com.example.careeix.utils.dto.ApplicationResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags ="카카오 API")
public class KaKaoController {

    private final KakaoServiceImpl kakaoService;

    public KaKaoController(KakaoServiceImpl kakaoService) {
        this.kakaoService = kakaoService;
    }

    // 회원가입
    @ResponseBody
    @ApiOperation(value = "카카오 엑세스 토큰 반환")
    @GetMapping("/oauth/kakao")
    public ApplicationResponse<MessageResponse> kakaoCallback(@RequestParam String code) throws BaseException {
        System.out.println(code);

        // 접속토큰 get
        String kakaoToken = kakaoService.getKakaoAccessToken(code);

        // 접속자 정보 get
        return ApplicationResponse.ok(MessageResponse.builder()
                .message(kakaoToken)
                .build());

    }








}

