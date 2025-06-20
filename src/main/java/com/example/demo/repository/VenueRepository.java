package com.example.demo.repository;

import com.example.demo.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 공연장 레포지토리
 * 콘서트홀, 극장, 체육관 등 공연장 정보의 데이터 액세스를 담당
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    
    /**
     * 공연장명 검색 (부분 일치)
     * @param name 검색할 공연장명
     * @return 공연장명에 해당 키워드가 포함된 공연장 목록
     */
    List<Venue> findByNameContaining(String name);
    
    /**
     * 주소로 공연장 검색 (부분 일치)
     * @param address 검색할 주소
     * @return 주소에 해당 키워드가 포함된 공연장 목록
     */
    List<Venue> findByAddressContaining(String address);
    
    /**
     * 최소 좌석 수 이상의 공연장 조회
     * @param minSeats 최소 좌석 수
     * @return 조건에 맞는 공연장 목록
     */
    List<Venue> findByTotalSeatsGreaterThanEqual(Integer minSeats);
    
    /**
     * 최대 좌석 수 이하의 공연장 조회
     * @param maxSeats 최대 좌석 수
     * @return 조건에 맞는 공연장 목록
     */
    List<Venue> findByTotalSeatsLessThanEqual(Integer maxSeats);
    
    /**
     * 좌석 수 범위로 공연장 조회
     * @param minSeats 최소 좌석 수
     * @param maxSeats 최대 좌석 수
     * @return 좌석 수가 범위 내에 있는 공연장 목록
     */
    @Query("SELECT v FROM Venue v WHERE v.totalSeats BETWEEN :minSeats AND :maxSeats")
    List<Venue> findByTotalSeatsBetween(@Param("minSeats") Integer minSeats, @Param("maxSeats") Integer maxSeats);
    
    /**
     * 키워드로 공연장 통합 검색 (이름 또는 주소)
     * @param keyword 검색 키워드
     * @return 이름 또는 주소에 키워드가 포함된 공연장 목록
     */
    @Query("SELECT v FROM Venue v WHERE v.name LIKE %:keyword% OR v.address LIKE %:keyword%")
    List<Venue> findByKeyword(@Param("keyword") String keyword);
}