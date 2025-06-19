package com.example.demo.service;

import com.example.demo.dto.auth.KakaoLoginResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.UserRole;
import com.example.demo.entity.enums.UserStatus;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Transactional
    public KakaoLoginResponse processKakaoLogin(String code) {
        String accessToken = getKakaoAccessToken(code);
        JsonNode userInfo = getKakaoUserInfo(accessToken);
        
        String kakaoId = userInfo.get("id").asText();
        String email = null;
        String nickname = null;
        
        if (userInfo.has("kakao_account")) {
            JsonNode kakaoAccount = userInfo.get("kakao_account");
            if (kakaoAccount.has("email")) {
                email = kakaoAccount.get("email").asText();
            }
            if (kakaoAccount.has("profile")) {
                JsonNode profile = kakaoAccount.get("profile");
                if (profile.has("nickname")) {
                    nickname = profile.get("nickname").asText();
                }
            }
        }

        Optional<User> existingUser = userRepository.findByProviderAndProviderId("kakao", kakaoId);
        
        User user;
        boolean isNewUser = false;
        
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            if (email != null && userRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("이미 다른 방법으로 가입된 이메일입니다.");
            }
            
            user = new User(email, nickname, "kakao", kakaoId, UserRole.USER, UserStatus.ACTIVE);
            user = userRepository.save(user);
            isNewUser = true;
        }

        String jwtToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name(), true);
        
        return new KakaoLoginResponse(jwtToken, null, user.getEmail(), user.getName(), "kakao", isNewUser);
    }

    private String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("client_secret", kakaoClientSecret);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("카카오 액세스 토큰 획득 실패", e);
        }
    }

    private JsonNode getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, String.class);
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 획득 실패", e);
        }
    }
}