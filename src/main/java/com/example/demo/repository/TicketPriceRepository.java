package com.example.demo.repository;

import com.example.demo.entity.Schedule;
import com.example.demo.entity.TicketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 티켓 가격 레포지토리
 * 공연 일정별 좌석 등급에 따른 티켓 가격 정보의 데이터 액세스를 담당
 */
@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {
    
    /**
     * 특정 공연 일정의 모든 티켓 가격 조회
     * @param schedule 공연 일정 엔티티
     * @return 해당 공연 일정의 티켓 가격 목록
     */
    List<TicketPrice> findBySchedule(Schedule schedule);
    
    /**
     * 공연 일정 ID로 티켓 가격 조회
     * @param scheduleId 공연 일정 ID
     * @return 해당 공연 일정의 티켓 가격 목록
     */
    List<TicketPrice> findByScheduleId(Long scheduleId);
    
    /**
     * 좌석 등급별 티켓 가격 조회
     * @param grade 좌석 등급 (VIP, R석, S석 등)
     * @return 해당 등급의 티켓 가격 목록
     */
    List<TicketPrice> findByGrade(String grade);
    
    /**
     * 특정 공연 일정의 특정 등급 티켓 가격 조회
     * @param schedule 공연 일정 엔티티
     * @param grade 좌석 등급
     * @return 해당 조건의 티켓 가격 (Optional)
     */
    Optional<TicketPrice> findByScheduleAndGrade(Schedule schedule, String grade);
    
    /**
     * 공연 일정 ID와 등급으로 티켓 가격 조회
     * @param scheduleId 공연 일정 ID
     * @param grade 좌석 등급
     * @return 해당 조건의 티켓 가격 (Optional)
     */
    Optional<TicketPrice> findByScheduleIdAndGrade(Long scheduleId, String grade);
    
    /**
     * 최소 가격 이상의 티켓 가격 조회
     * @param minPrice 최소 가격
     * @return 조건에 맞는 티켓 가격 목록
     */
    List<TicketPrice> findByPriceGreaterThanEqual(BigDecimal minPrice);
    
    /**
     * 최대 가격 이하의 티켓 가격 조회
     * @param maxPrice 최대 가격
     * @return 조건에 맞는 티켓 가격 목록
     */
    List<TicketPrice> findByPriceLessThanEqual(BigDecimal maxPrice);
    
    /**
     * 가격 범위 내의 티켓 가격 조회
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 가격 범위 내의 티켓 가격 목록
     */
    List<TicketPrice> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * 특정 이벤트의 모든 티켓 가격 조회
     * @param eventId 이벤트 ID
     * @return 해당 이벤트의 티켓 가격 목록
     */
    @Query("SELECT tp FROM TicketPrice tp WHERE tp.schedule.event.id = :eventId")
    List<TicketPrice> findByEventId(@Param("eventId") Long eventId);
    
    /**
     * 특정 이벤트의 특정 등급 티켓 가격 조회
     * @param eventId 이벤트 ID
     * @param grade 좌석 등급
     * @return 조건에 맞는 티켓 가격 목록
     */
    @Query("SELECT tp FROM TicketPrice tp WHERE tp.schedule.event.id = :eventId AND tp.grade = :grade")
    List<TicketPrice> findByEventIdAndGrade(@Param("eventId") Long eventId, @Param("grade") String grade);
    
    /**
     * 특정 공연 일정의 모든 좌석 등급 조회 (중복 제거)
     * @param scheduleId 공연 일정 ID
     * @return 해당 공연 일정의 좌석 등급 목록 (정렬됨)
     */
    @Query("SELECT DISTINCT tp.grade FROM TicketPrice tp WHERE tp.schedule.id = :scheduleId ORDER BY tp.grade")
    List<String> findDistinctGradesByScheduleId(@Param("scheduleId") Long scheduleId);
    
    /**
     * 특정 공연 일정의 최저 티켓 가격 조회
     * @param scheduleId 공연 일정 ID
     * @return 해당 공연 일정의 최저 가격
     */
    @Query("SELECT MIN(tp.price) FROM TicketPrice tp WHERE tp.schedule.id = :scheduleId")
    BigDecimal findMinPriceByScheduleId(@Param("scheduleId") Long scheduleId);
    
    /**
     * 특정 공연 일정의 최고 티켓 가격 조회
     * @param scheduleId 공연 일정 ID
     * @return 해당 공연 일정의 최고 가격
     */
    @Query("SELECT MAX(tp.price) FROM TicketPrice tp WHERE tp.schedule.id = :scheduleId")
    BigDecimal findMaxPriceByScheduleId(@Param("scheduleId") Long scheduleId);
}