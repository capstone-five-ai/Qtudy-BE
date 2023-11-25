package com.app.domain.member.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.util.DateTimeUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberType memberType;
    // OAuth 타입 관리
    @Column(unique = true, length = 50, nullable = false)
    private String email;

//    @Column(length = 200)
//    private String password;
//  카카오 로그인만 제공, 비밀번호 필요 X
    // 소셜 로그인 인증 같은 경우 비밀번호를 직접 다루지 않기 때문에 nullable
    @Column(nullable = false, length = 100)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;
    // 현재는 큰 의미 없음
    @Column(length = 250)
    private String refreshToken;
    // 리프레쉬 토큰
    private LocalDateTime tokenExpirationTime;
    // 토큰 만료 시간
    /*@Builder
    public Member(MemberType memberType, String email, String password, String nickName,
                  String profile, Role role) {
        this.memberType = memberType;
        this.email = email;
//        this.password = password;
        this.nickName = nickName;
//        this.profile = profile;
        this.role = role;
    }*/

    public void updateRefreshToken(JwtTokenDto jwtTokenDto) {
        this.refreshToken = jwtTokenDto.getRefreshToken();
        this.tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(jwtTokenDto.getRefreshTokenExpireTime());
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }
}
