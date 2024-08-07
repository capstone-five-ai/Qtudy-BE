package com.app.web.kakaotoken.controller;

import com.app.web.kakaotoken.client.KakaoTokenClient;
import com.app.web.kakaotoken.dto.KakaoTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 카카오 로그인 flow에서 사용
 */

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {

    private final KakaoTokenClient kakaoTokenClient;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @GetMapping("/kakao/login")
    public String login() {
        return "loginForm";
    }
    // login 페이지에서 카카오 로그인 버튼 클릭 시 -> oauth/kako/callback으로 리다이렉트(개발자 사이트에서 직접 설정함)
    // loginCallback 메서드 실행 -> reqeustKakaoToken 메서드에서 FeignClient 실행
    // -> 카카오 REST API의 토큰 받기(/oauth/token) (Host: kauth.kakao.com)로 연결 -> 토큰 받아와서 kakoToken에 저장
    // token_type , access_token , expires_in, refresh_token, expires_in,
    // scope(account_email, profile_image, profile_nickname) 출력
    //redirect 처리 메서드
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<KakaoTokenDto.Response> loginCallback(@RequestParam("code")String code, @RequestParam("redirectUri") String redirectUri) {
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
//                .redirect_uri("http://54.180.142.157/oauth/kakao/callback")
//                .redirect_uri("http://localhost:5173/oauth/kakao/callback")
                .redirect_uri(redirectUri)
                .build();
        KakaoTokenDto.Response kakaoToken = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);
//        return "kakao token : " + kakaoToken;
        return ResponseEntity.ok(kakaoToken);
    }
}
