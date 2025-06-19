package com.example.demo.service;

import com.example.demo.dto.auth.GuestLoginRequest;
import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.dto.auth.KakaoLoginResponse;
import com.example.demo.entity.Guest;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.UserRole;
import com.example.demo.repository.GuestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;
    
    @Transactional
    public LoginResponse memberLogin(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
        
        if (!user.isActive()) {
            throw new RuntimeException("비활성화된 사용자입니다.");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name(), true);
        
        return LoginResponse.of(token, user.getId(), user.getEmail(), user.getName(), user.getRole());
    }
    
    @Transactional
    public LoginResponse guestLogin(GuestLoginRequest request) {
        Guest guest = guestRepository.findByNameAndPhoneNumber(request.getName(), request.getPhoneNumber())
                .orElseGet(() -> {
                    Guest newGuest = new Guest(request.getName(), request.getPhoneNumber());
                    return guestRepository.save(newGuest);
                });
        
        String token = jwtUtil.generateToken(guest.getId(), "", UserRole.USER.name(), false);
        
        return new LoginResponse(token, guest.getId(), "", guest.getName(), UserRole.USER, false);
    }
    
    @Transactional
    public KakaoLoginResponse kakaoLogin(String code) {
        return kakaoService.processKakaoLogin(code);
    }
}