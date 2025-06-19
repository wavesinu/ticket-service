package com.example.demo.dto.auth;

import com.example.demo.entity.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    
    private String accessToken;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;
    private String name;
    private UserRole role;
    private boolean isMember;
    
    public LoginResponse(String accessToken, Long userId, String email, String name, UserRole role, boolean isMember) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
        this.isMember = isMember;
    }
    
    public static LoginResponse of(String accessToken, Long userId, String email, String name, UserRole role) {
        return new LoginResponse(accessToken, userId, email, name, role, true);
    }
}