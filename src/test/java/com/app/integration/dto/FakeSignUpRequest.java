package com.app.integration.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FakeSignUpRequest {

    private String nickname;
    private String email;

    public FakeSignUpRequest(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
