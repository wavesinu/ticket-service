package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import com.example.demo.entity.Schedule;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.VenueSeat;
import com.example.demo.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 티켓 레포지토리
 * 개별 티켓의 예약 상태, 좌석 정보, 동시성 제어 등의 데이터 액세스를 담당
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    /**
     * 특정 공연 일정의 모든 티켓 조회
     * @param schedule 공연 일정 엔티티
     * @return 해당 공연 일정의 티켓 목록
     */
    List<Ticket> findBySchedule(Schedule schedule);
    
    /**
     * 공연 일정 ID로 티켓 조회
     * @param scheduleId 공연 일정 ID
     * @return 해당 공연 일정의 티켓 목록
     */
    List<Ticket> findByScheduleId(Long scheduleId);
    
    /**
     * 티켓 상태별 조회
     * @param status 티켓 상태 (판매 가능, 예약됨, 판매됨 등)
     * @return 해당 상태의 티켓 목록
     */
    List<Ticket> findByStatus(TicketStatus status);
    
    /**
     * 특정 공연 일정의 특정 상태 티켓 조회
     * @param schedule 공연 일정 엔티티
     * @param status 티켓 상태
     * @return 조건에 맞는 티켓 목록
     */
    List<Ticket> findByScheduleAndStatus(Schedule schedule, TicketStatus status);
    
    /**
     * 공연 일정 ID와 상태로 티켓 조회
     * @param scheduleId 공연 일정 ID
     * @param status 티켓 상태
     * @return 조건에 맞는 티켓 목록
     */
    List<Ticket> findByScheduleIdAndStatus(Long scheduleId, TicketStatus status);
    
    /**
     * 특정 예약의 모든 티켓 조회
     * @param reservation 예약 엔티티
     * @return 해당 예약의 티켓 목록
     */
    List<Ticket> findByReservation(Reservation reservation);
    
    /**
     * 예약 ID로 티켓 조회
     * @param reservationId 예약 ID
     * @return 해당 예약의 티켓 목록
     */
    List<Ticket> findByReservationId(Long reservationId);
    
    /**
     * 특정 좌석의 모든 티켓 조회 (모든 공연 일정)
     * @param venueSeat 공연장 좌석 엔티티
     * @return 해당 좌석의 티켓 목록
     */
    List<Ticket> findByVenueSeat(VenueSeat venueSeat);
    
    /**
     * 특정 공연 일정의 특정 좌석 티켓 조회
     * @param schedule 공연 일정 엔티티
     * @param venueSeat 공연장 좌석 엔티티
     * @return 해당 조건의 티켓 (Optional)
     */
    Optional<Ticket> findByScheduleAndVenueSeat(Schedule schedule, VenueSeat venueSeat);
    
    /**
     * 공연 일정 ID와 좌석 ID로 티켓 조회
     * @param scheduleId 공연 일정 ID
     * @param venueSeatId 공연장 좌석 ID
     * @return 해당 조건의 티켓 (Optional)
     */
    Optional<Ticket> findByScheduleIdAndVenueSeatId(Long scheduleId, Long venueSeatId);
    
    /**
     * 비관적 락을 사용한 티켓 조회 (동시성 제어)
     * @param ticketId 티켓 ID
     * @return 락이 걸린 티켓 (Optional)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Ticket t WHERE t.id = :ticketId")
    Optional<Ticket> findByIdWithLock(@Param("ticketId") Long ticketId);
    
    /**
     * 특정 공연 일정의 특정 상태 티켓 수 조회
     * @param scheduleId 공연 일정 ID
     * @param status 티켓 상태
     * @return 해당 조건의 티켓 수
     */
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.status = :status")
    long countByScheduleIdAndStatus(@Param("scheduleId") Long scheduleId, @Param("status") TicketStatus status);
    
    /**
     * 만료된 예약 티켓 조회 (예약 상태이면서 만료 시간 초과)
     * @param now 현재 시점
     * @return 만료된 예약 티켓 목록
     */
    @Query("SELECT t FROM Ticket t WHERE t.status = 'RESERVED' AND t.expiresAt < :now")
    List<Ticket> findExpiredReservedTickets(@Param("now") LocalDateTime now);
    
    /**
     * 특정 공연 일정의 구매 가능한 티켓을 좌석 순으로 조회
     * @param scheduleId 공연 일정 ID
     * @return 구매 가능한 티켓 목록 (좌석 순 정렬)
     */
    @Query("SELECT t FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.status = 'AVAILABLE' ORDER BY t.venueSeat.section, t.venueSeat.seatRow, t.venueSeat.seatNumber")
    List<Ticket> findAvailableTicketsByScheduleOrderBySeat(@Param("scheduleId") Long scheduleId);
    
    /**
     * 특정 이벤트의 모든 티켓 조회
     * @param eventId 이벤트 ID
     * @return 해당 이벤트의 티켓 목록
     */
    @Query("SELECT t FROM Ticket t WHERE t.schedule.event.id = :eventId")
    List<Ticket> findByEventId(@Param("eventId") Long eventId);
    
    /**
     * 특정 이벤트의 특정 상태 티켓 조회
     * @param eventId 이벤트 ID
     * @param status 티켓 상태
     * @return 조건에 맞는 티켓 목록
     */
    @Query("SELECT t FROM Ticket t WHERE t.schedule.event.id = :eventId AND t.status = :status")
    List<Ticket> findByEventIdAndStatus(@Param("eventId") Long eventId, @Param("status") TicketStatus status);
    
    /**
     * 특정 공연 일정의 특정 등급 구매 가능한 티켓 조회
     * @param scheduleId 공연 일정 ID
     * @param grade 좌석 등급 (VIP, R석, S석 등)
     * @return 조건에 맞는 구매 가능한 티켓 목록
     */
    @Query("SELECT t FROM Ticket t WHERE t.venueSeat.grade = :grade AND t.schedule.id = :scheduleId AND t.status = 'AVAILABLE'")
    List<Ticket> findAvailableTicketsByScheduleIdAndGrade(@Param("scheduleId") Long scheduleId, @Param("grade") String grade);
}