package com.example.demo.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String name;
    private String provider;
    private boolean isNewUser;
}