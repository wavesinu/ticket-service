package com.example.demo.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {
    
    // 일반적인 에러
    _BAD_REQUEST("E400", "잘못된 요청입니다."),
    _UNAUTHORIZED("E401", "인증이 필요합니다."),
    _FORBIDDEN("E403", "접근 권한이 없습니다."),
    _NOT_FOUND("E404", "요청한 리소스를 찾을 수 없습니다."),
    _INTERNAL_SERVER_ERROR("E500", "서버 내부 오류가 발생했습니다."),
    
    // 인증 관련 에러
    _INVALID_TOKEN("E1001", "유효하지 않은 토큰입니다."),
    _EXPIRED_TOKEN("E1002", "만료된 토큰입니다."),
    _LOGIN_FAILED("E1003", "로그인에 실패했습니다."),
    _INVALID_CREDENTIALS("E1004", "잘못된 인증 정보입니다."),
    _KAKAO_LOGIN_FAILED("E1005", "카카오 로그인에 실패했습니다."),
    
    // 사용자 관련 에러
    _USER_NOT_FOUND("E2001", "사용자를 찾을 수 없습니다."),
    _USER_ALREADY_EXISTS("E2002", "이미 존재하는 사용자입니다."),
    _INVALID_USER_INFO("E2003", "잘못된 사용자 정보입니다."),
    
    // 예약 관련 에러
    _RESERVATION_NOT_FOUND("E3001", "예약을 찾을 수 없습니다."),
    _RESERVATION_ALREADY_EXISTS("E3002", "이미 존재하는 예약입니다."),
    _RESERVATION_NOT_AVAILABLE("E3003", "예약할 수 없는 상태입니다."),
    _SEAT_NOT_AVAILABLE("E3004", "선택한 좌석을 예약할 수 없습니다."),
    _RESERVATION_LIMIT_EXCEEDED("E3005", "예약 한도를 초과했습니다."),
    
    // 이벤트 관련 에러
    _EVENT_NOT_FOUND("E4001", "이벤트를 찾을 수 없습니다."),
    _EVENT_NOT_AVAILABLE("E4002", "이벤트가 예약 가능한 상태가 아닙니다."),
    _EVENT_SOLD_OUT("E4003", "이벤트가 매진되었습니다."),
    
    // 결제 관련 에러
    _PAYMENT_FAILED("E5001", "결제에 실패했습니다."),
    _PAYMENT_NOT_FOUND("E5002", "결제 정보를 찾을 수 없습니다."),
    _PAYMENT_ALREADY_PROCESSED("E5003", "이미 처리된 결제입니다."),
    _INSUFFICIENT_BALANCE("E5004", "잔액이 부족합니다."),
    
    // 유효성 검사 에러
    _INVALID_INPUT("E6001", "입력값이 유효하지 않습니다."),
    _MISSING_REQUIRED_FIELD("E6002", "필수 입력값이 누락되었습니다."),
    _INVALID_EMAIL_FORMAT("E6003", "이메일 형식이 올바르지 않습니다."),
    _INVALID_PHONE_FORMAT("E6004", "전화번호 형식이 올바르지 않습니다.");
    
    private final String code;
    private final String message;
    
    @Override
    public ReasonDto getReasonHttpStatus() {
        HttpStatus httpStatus = switch (code.charAt(1)) {
            case '4' -> switch (code.substring(1, 4)) {
                case "400" -> HttpStatus.BAD_REQUEST;
                case "401" -> HttpStatus.UNAUTHORIZED;
                case "403" -> HttpStatus.FORBIDDEN;
                case "404" -> HttpStatus.NOT_FOUND;
                default -> HttpStatus.BAD_REQUEST;
            };
            case '5' -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.BAD_REQUEST;
        };
        
        return ReasonDto.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}