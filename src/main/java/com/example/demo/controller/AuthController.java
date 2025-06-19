package com.example.demo.controller;

import com.example.demo.dto.auth.GuestLoginRequest;
import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.dto.auth.KakaoLoginResponse;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/member/login")
    public ResponseEntity<LoginResponse> memberLogin(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.memberLogin(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/guest/login")
    public ResponseEntity<LoginResponse> guestLogin(@Valid @RequestBody GuestLoginRequest request) {
        try {
            LoginResponse response = authService.guestLogin(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/kakao/login")
    public ResponseEntity<String> kakaoLogin() {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + "{your-kakao-client-id}"
                + "&redirect_uri=" + "{your-redirect-uri}"
                + "&response_type=code"
                + "&scope=profile_nickname,profile_image,account_email";
        return ResponseEntity.ok(kakaoAuthUrl);
    }
    
    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoLoginResponse> kakaoCallback(@RequestParam String code) {
        try {
            KakaoLoginResponse response = authService.kakaoLogin(code);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout successful");
    }
}