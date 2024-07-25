package com.app.fake;

import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Profile(value = {"local", "test"})
@RestController
public class FakeLoginController {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    @PostMapping("/api/fake/login")
    public ResponseEntity<String> fakeSignUp(@RequestBody FakeSignUpRequest request) {
        Member member = memberService.findMemberByEmail(request.getEmail()).orElseGet(
                () -> memberService.registerMember(Member.builder()
                        .nickName(request.getNickname())
                        .email(request.getEmail())
                        .memberType(MemberType.KAKAO)
                        .role(Role.USER)
                        .build())
        );

        JwtTokenDto jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), member.getRole());
        member.updateRefreshToken(jwtTokenDto);
        return ResponseEntity.ok(jwtTokenDto.getAccessToken());
    }
}
