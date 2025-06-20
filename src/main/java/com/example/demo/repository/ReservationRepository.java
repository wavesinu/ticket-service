package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 예약 레포지토리
 * 티켓 예약 정보와 예약 상태 관리의 데이터 액세스를 담당
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    /**
     * 특정 사용자의 모든 예약 조회
     * @param user 사용자 엔티티
     * @return 해당 사용자의 예약 목록
     */
    List<Reservation> findByUser(User user);
    
    /**
     * 사용자 ID로 예약 조회
     * @param userId 사용자 ID
     * @return 해당 사용자의 예약 목록
     */
    List<Reservation> findByUserId(Long userId);
    
    /**
     * 예약 상태별 조회
     * @param status 예약 상태 (결제 대기, 확정, 취소 등)
     * @return 해당 상태의 예약 목록
     */
    List<Reservation> findByStatus(ReservationStatus status);
    
    /**
     * 특정 사용자의 특정 상태 예약 조회
     * @param userId 사용자 ID
     * @param status 예약 상태
     * @return 조건에 맞는 예약 목록
     */
    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);
    
    /**
     * 만료된 예약 조회 (특정 상태이면서 생성일이 기준 시점 이전)
     * @param status 예약 상태
     * @param expiredBefore 만료 기준 시점
     * @return 만료된 예약 목록
     */
    @Query("SELECT r FROM Reservation r WHERE r.status = :status AND r.createdAt < :expiredBefore")
    List<Reservation> findExpiredReservations(@Param("status") ReservationStatus status, @Param("expiredBefore") LocalDateTime expiredBefore);
    
    /**
     * 특정 사용자의 예약을 최신순으로 조회
     * @param userId 사용자 ID
     * @return 해당 사용자의 예약 목록 (생성일 내림차순 정렬)
     */
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Reservation> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    /**
     * 특정 공연 일정의 모든 예약 조회
     * @param scheduleId 공연 일정 ID
     * @return 해당 공연 일정의 예약 목록
     */
    @Query("SELECT r FROM Reservation r JOIN r.tickets t WHERE t.schedule.id = :scheduleId")
    List<Reservation> findByScheduleId(@Param("scheduleId") Long scheduleId);
    
    /**
     * 특정 이벤트의 모든 예약 조회
     * @param eventId 이벤트 ID
     * @return 해당 이벤트의 예약 목록
     */
    @Query("SELECT r FROM Reservation r JOIN r.tickets t WHERE t.schedule.event.id = :eventId")
    List<Reservation> findByEventId(@Param("eventId") Long eventId);
    
    /**
     * 예약 상태별 건수 조회
     * @param status 예약 상태
     * @return 해당 상태의 예약 건수
     */
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = :status")
    long countByStatus(@Param("status") ReservationStatus status);
    
    /**
     * 결제 대기 상태에서 특정 시점 이전에 생성된 예약 조회 (만료 처리 대상)
     * @param cutoffTime 만료 기준 시점
     * @return 만료 처리 대상 예약 목록
     */
    @Query("SELECT r FROM Reservation r WHERE r.status = 'PENDING_PAYMENT' AND r.createdAt < :cutoffTime")
    List<Reservation> findPendingPaymentReservationsBeforeTime(@Param("cutoffTime") LocalDateTime cutoffTime);
}