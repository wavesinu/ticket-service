package com.example.demo.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    
    // 일반적인 성공 응답
    _OK("S200", "요청이 성공했습니다."),
    _CREATED("S201", "리소스가 성공적으로 생성되었습니다."),
    
    // 인증 관련 성공
    _LOGIN_SUCCESS("S2001", "로그인에 성공했습니다."),
    _LOGOUT_SUCCESS("S2002", "로그아웃에 성공했습니다."),
    _TOKEN_REFRESH_SUCCESS("S2003", "토큰 갱신에 성공했습니다."),
    
    // 사용자 관련 성공
    _USER_CREATED("S3001", "사용자가 성공적으로 생성되었습니다."),
    _USER_UPDATED("S3002", "사용자 정보가 성공적으로 업데이트되었습니다."),
    _USER_DELETED("S3003", "사용자가 성공적으로 삭제되었습니다."),
    
    // 예약 관련 성공
    _RESERVATION_CREATED("S4001", "예약이 성공적으로 생성되었습니다."),
    _RESERVATION_UPDATED("S4002", "예약이 성공적으로 수정되었습니다."),
    _RESERVATION_CANCELLED("S4003", "예약이 성공적으로 취소되었습니다."),
    
    // 결제 관련 성공
    _PAYMENT_SUCCESS("S5001", "결제가 성공적으로 완료되었습니다."),
    _PAYMENT_CANCELLED("S5002", "결제가 성공적으로 취소되었습니다.");
    
    private final String code;
    private final String message;
    
    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .httpStatus(HttpStatus.OK)
                .code(code)
                .message(message)
                .build();
    }
}