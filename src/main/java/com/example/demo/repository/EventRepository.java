package com.example.demo.repository;

import com.example.demo.entity.Event;
import com.example.demo.entity.Venue;
import com.example.demo.entity.enums.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 이벤트 레포지토리
 * 콘서트, 뮤지컬, 연극 등 각종 이벤트 정보의 데이터 액세스를 담당
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    /**
     * 특정 공연장의 모든 이벤트 조회
     * @param venue 공연장 엔티티
     * @return 해당 공연장의 이벤트 목록
     */
    List<Event> findByVenue(Venue venue);
    
    /**
     * 공연장 ID로 이벤트 조회
     * @param venueId 공연장 ID
     * @return 해당 공연장의 이벤트 목록
     */
    List<Event> findByVenueId(Long venueId);
    
    /**
     * 카테고리별 이벤트 조회
     * @param category 이벤트 카테고리 (콘서트, 뮤지컬 등)
     * @return 해당 카테고리의 이벤트 목록
     */
    List<Event> findByCategory(EventCategory category);
    
    /**
     * 아티스트명으로 이벤트 조회 (정확히 일치)
     * @param artist 아티스트명
     * @return 해당 아티스트의 이벤트 목록
     */
    List<Event> findByArtist(String artist);
    
    /**
     * 이벤트명 검색 (부분 일치)
     * @param name 검색할 이벤트명
     * @return 이벤트명에 해당 키워드가 포함된 이벤트 목록
     */
    List<Event> findByNameContaining(String name);
    
    /**
     * 아티스트명 검색 (부분 일치)
     * @param artist 검색할 아티스트명
     * @return 아티스트명에 해당 키워드가 포함된 이벤트 목록
     */
    List<Event> findByArtistContaining(String artist);
    
    /**
     * 카테고리와 연령 제한으로 이벤트 조회
     * @param category 이벤트 카테고리
     * @param age 사용자 나이 (이 나이 이상 관람 가능한 이벤트 조회)
     * @return 조건에 맞는 이벤트 목록
     */
    @Query("SELECT e FROM Event e WHERE e.category = :category AND e.ageRestriction <= :age")
    List<Event> findByCategoryAndAgeRestrictionLessThanEqual(@Param("category") EventCategory category, @Param("age") Integer age);
    
    /**
     * 현재 판매 중인 이벤트 조회
     * @return 현재 티켓 판매가 열린 이벤트 목록
     */
    @Query("SELECT e FROM Event e JOIN e.schedules s WHERE s.saleOpenDatetime <= CURRENT_TIMESTAMP")
    List<Event> findEventsWithOpenSales();
    
    /**
     * 공연장과 카테고리로 이벤트 조회
     * @param venueId 공연장 ID
     * @param category 이벤트 카테고리
     * @return 조건에 맞는 이벤트 목록
     */
    @Query("SELECT e FROM Event e WHERE e.venue.id = :venueId AND e.category = :category")
    List<Event> findByVenueIdAndCategory(@Param("venueId") Long venueId, @Param("category") EventCategory category);
}