package com.example.demo.repository;

import com.example.demo.entity.Event;
import com.example.demo.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 공연 일정 레포지토리
 * 이벤트의 구체적인 공연 일시와 티켓 판매 일정 정보의 데이터 액세스를 담당
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    /**
     * 특정 이벤트의 모든 공연 일정 조회
     * @param event 이벤트 엔티티
     * @return 해당 이벤트의 공연 일정 목록
     */
    List<Schedule> findByEvent(Event event);
    
    /**
     * 이벤트 ID로 공연 일정 조회
     * @param eventId 이벤트 ID
     * @return 해당 이벤트의 공연 일정 목록
     */
    List<Schedule> findByEventId(Long eventId);
    
    /**
     * 특정 시점 이후의 공연 일정 조회
     * @param dateTime 기준 시점
     * @return 기준 시점 이후의 공연 일정 목록
     */
    List<Schedule> findByShowDatetimeAfter(LocalDateTime dateTime);
    
    /**
     * 특정 시점 이전의 공연 일정 조회
     * @param dateTime 기준 시점
     * @return 기준 시점 이전의 공연 일정 목록
     */
    List<Schedule> findByShowDatetimeBefore(LocalDateTime dateTime);
    
    /**
     * 특정 기간 내의 공연 일정 조회
     * @param startDateTime 시작 시점
     * @param endDateTime 종료 시점
     * @return 해당 기간 내의 공연 일정 목록
     */
    List<Schedule> findByShowDatetimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * 티켓 판매 시작 시점 이후의 일정 조회
     * @param dateTime 기준 시점
     * @return 기준 시점 이후에 티켓 판매가 시작되는 일정 목록
     */
    List<Schedule> findBySaleOpenDatetimeAfter(LocalDateTime dateTime);
    
    /**
     * 티켓 판매 시작 시점 이전의 일정 조회
     * @param dateTime 기준 시점
     * @return 기준 시점 이전에 티켓 판매가 시작된 일정 목록
     */
    List<Schedule> findBySaleOpenDatetimeBefore(LocalDateTime dateTime);
    
    /**
     * 현재 티켓 구매 가능한 공연 일정 조회
     * @param now 현재 시점
     * @return 티켓 판매가 열렸고 아직 공연이 끝나지 않은 일정 목록
     */
    @Query("SELECT s FROM Schedule s WHERE s.saleOpenDatetime <= :now AND s.showDatetime > :now")
    List<Schedule> findAvailableSchedules(@Param("now") LocalDateTime now);
    
    /**
     * 특정 이벤트의 향후 공연 일정 조회
     * @param eventId 이벤트 ID
     * @param now 현재 시점
     * @return 해당 이벤트의 향후 공연 일정 목록
     */
    @Query("SELECT s FROM Schedule s WHERE s.event.id = :eventId AND s.showDatetime > :now")
    List<Schedule> findUpcomingSchedulesByEventId(@Param("eventId") Long eventId, @Param("now") LocalDateTime now);
    
    /**
     * 현재 구매 가능한 공연 일정을 공연 시간 순으로 조회
     * @param now 현재 시점
     * @return 구매 가능한 공연 일정 목록 (공연 시간 오름차순 정렬)
     */
    @Query("SELECT s FROM Schedule s WHERE s.saleOpenDatetime <= :now AND s.showDatetime > :now ORDER BY s.showDatetime ASC")
    List<Schedule> findAvailableSchedulesOrderByShowDatetime(@Param("now") LocalDateTime now);
    
    /**
     * 특정 기간의 공연 일정을 시간순으로 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 공연 일정 목록 (공연 시간 오름차순 정렬)
     */
    @Query("SELECT s FROM Schedule s WHERE s.showDatetime BETWEEN :startDate AND :endDate ORDER BY s.showDatetime ASC")
    List<Schedule> findSchedulesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}