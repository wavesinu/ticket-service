package com.example.demo.repository;

import com.example.demo.entity.Payment;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 결제 레포지토리
 * 티켓 예약에 대한 결제 정보와 결제 상태 관리의 데이터 액세스를 담당
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * 특정 예약의 결제 정보 조회
     * @param reservation 예약 엔티티
     * @return 해당 예약의 결제 목록
     */
    List<Payment> findByReservation(Reservation reservation);
    
    /**
     * 예약 ID로 결제 정보 조회
     * @param reservationId 예약 ID
     * @return 해당 예약의 결제 정보 (Optional)
     */
    Optional<Payment> findByReservationId(Long reservationId);
    
    /**
     * 결제 상태별 조회
     * @param status 결제 상태 (대기, 완료, 실패, 취소 등)
     * @return 해당 상태의 결제 목록
     */
    List<Payment> findByStatus(PaymentStatus status);
    
    /**
     * 결제 수단별 조회
     * @param paymentMethod 결제 수단 (카드, 계좌이체, 간편결제 등)
     * @return 해당 결제 수단의 결제 목록
     */
    List<Payment> findByPaymentMethod(String paymentMethod);
    
    /**
     * PG사 거래 ID로 결제 조회
     * @param pgTransactionId PG사에서 발급한 거래 ID
     * @return 해당 거래 ID의 결제 정보 (Optional)
     */
    Optional<Payment> findByPgTransactionId(String pgTransactionId);
    
    /**
     * 특정 사용자의 모든 결제 조회
     * @param userId 사용자 ID
     * @return 해당 사용자의 결제 목록
     */
    @Query("SELECT p FROM Payment p WHERE p.reservation.user.id = :userId")
    List<Payment> findByUserId(@Param("userId") Long userId);
    
    /**
     * 특정 사용자의 특정 상태 결제 조회
     * @param userId 사용자 ID
     * @param status 결제 상태
     * @return 조건에 맞는 결제 목록
     */
    @Query("SELECT p FROM Payment p WHERE p.reservation.user.id = :userId AND p.status = :status")
    List<Payment> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") PaymentStatus status);
    
    /**
     * 특정 상태의 특정 기간 결제 조회
     * @param status 결제 상태
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 조건에 맞는 결제 목록
     */
    @Query("SELECT p FROM Payment p WHERE p.status = :status AND p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findByStatusAndCreatedAtBetween(@Param("status") PaymentStatus status, 
                                                  @Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate);
    
    /**
     * 특정 기간의 완료된 결제 총액 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 결제 완료 총액
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED' AND p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalCompletedPaymentAmount(@Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);
    
    /**
     * 결제 상태별 건수 조회
     * @param status 결제 상태
     * @return 해당 상태의 결제 건수
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    long countByStatus(@Param("status") PaymentStatus status);
    
    /**
     * 결제 대기 상태에서 특정 시점 이전에 생성된 결제 조회 (만료 처리 대상)
     * @param cutoffTime 만료 기준 시점
     * @return 만료 처리 대상 결제 목록
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' AND p.createdAt < :cutoffTime")
    List<Payment> findPendingPaymentsBeforeTime(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    /**
     * 특정 결제 수단의 특정 기간 결제를 최신순으로 조회
     * @param method 결제 수단
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 조건에 맞는 결제 목록 (생성일 내림차순 정렬)
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = :method AND p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
    List<Payment> findByPaymentMethodAndDateRange(@Param("method") String method, 
                                                  @Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate);
}