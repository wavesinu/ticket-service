package com.example.demo.repository;

import com.example.demo.entity.Venue;
import com.example.demo.entity.VenueSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 공연장 좌석 레포지토리
 * 공연장의 좌석 배치, 구역, 등급 정보의 데이터 액세스를 담당
 */
@Repository
public interface VenueSeatRepository extends JpaRepository<VenueSeat, Long> {
    
    /**
     * 특정 공연장의 모든 좌석 조회
     * @param venue 공연장 엔티티
     * @return 해당 공연장의 좌석 목록
     */
    List<VenueSeat> findByVenue(Venue venue);
    
    /**
     * 공연장 ID로 좌석 조회
     * @param venueId 공연장 ID
     * @return 해당 공연장의 좌석 목록
     */
    List<VenueSeat> findByVenueId(Long venueId);
    
    /**
     * 좌석 구역별 조회
     * @param section 좌석 구역 (1층, 2층, 발코니 등)
     * @return 해당 구역의 좌석 목록
     */
    List<VenueSeat> findBySection(String section);
    
    /**
     * 좌석 등급별 조회
     * @param grade 좌석 등급 (VIP, R석, S석 등)
     * @return 해당 등급의 좌석 목록
     */
    List<VenueSeat> findByGrade(String grade);
    
    /**
     * 특정 공연장의 특정 구역 좌석 조회
     * @param venue 공연장 엔티티
     * @param section 좌석 구역
     * @return 조건에 맞는 좌석 목록
     */
    List<VenueSeat> findByVenueAndSection(Venue venue, String section);
    
    /**
     * 공연장 ID와 구역으로 좌석 조회
     * @param venueId 공연장 ID
     * @param section 좌석 구역
     * @return 조건에 맞는 좌석 목록
     */
    List<VenueSeat> findByVenueIdAndSection(Long venueId, String section);
    
    /**
     * 특정 공연장의 특정 등급 좌석 조회
     * @param venue 공연장 엔티티
     * @param grade 좌석 등급
     * @return 조건에 맞는 좌석 목록
     */
    List<VenueSeat> findByVenueAndGrade(Venue venue, String grade);
    
    /**
     * 공연장 ID와 등급으로 좌석 조회
     * @param venueId 공연장 ID
     * @param grade 좌석 등급
     * @return 조건에 맞는 좌석 목록
     */
    List<VenueSeat> findByVenueIdAndGrade(Long venueId, String grade);
    
    /**
     * 구체적인 좌석 위치로 좌석 조회 (공연장, 구역, 열, 번호)
     * @param venue 공연장 엔티티
     * @param section 좌석 구역
     * @param seatRow 좌석 열
     * @param seatNumber 좌석 번호
     * @return 해당 위치의 좌석 (Optional)
     */
    Optional<VenueSeat> findByVenueAndSectionAndSeatRowAndSeatNumber(Venue venue, String section, String seatRow, String seatNumber);
    
    /**
     * ID와 구체적인 좌석 위치로 좌석 조회
     * @param venueId 공연장 ID
     * @param section 좌석 구역
     * @param seatRow 좌석 열
     * @param seatNumber 좌석 번호
     * @return 해당 위치의 좌석 (Optional)
     */
    Optional<VenueSeat> findByVenueIdAndSectionAndSeatRowAndSeatNumber(Long venueId, String section, String seatRow, String seatNumber);
    
    /**
     * 공연장, 구역, 등급으로 좌석 조회
     * @param venueId 공연장 ID
     * @param section 좌석 구역
     * @param grade 좌석 등급
     * @return 조건에 맞는 좌석 목록
     */
    @Query("SELECT vs FROM VenueSeat vs WHERE vs.venue.id = :venueId AND vs.section = :section AND vs.grade = :grade")
    List<VenueSeat> findByVenueIdAndSectionAndGrade(@Param("venueId") Long venueId, 
                                                    @Param("section") String section, 
                                                    @Param("grade") String grade);
    
    /**
     * 특정 공연장의 모든 구역 목록 조회 (중복 제거)
     * @param venueId 공연장 ID
     * @return 해당 공연장의 구역 목록 (정렬됨)
     */
    @Query("SELECT DISTINCT vs.section FROM VenueSeat vs WHERE vs.venue.id = :venueId ORDER BY vs.section")
    List<String> findDistinctSectionsByVenueId(@Param("venueId") Long venueId);
    
    /**
     * 특정 공연장의 모든 좌석 등급 목록 조회 (중복 제거)
     * @param venueId 공연장 ID
     * @return 해당 공연장의 좌석 등급 목록 (정렬됨)
     */
    @Query("SELECT DISTINCT vs.grade FROM VenueSeat vs WHERE vs.venue.id = :venueId ORDER BY vs.grade")
    List<String> findDistinctGradesByVenueId(@Param("venueId") Long venueId);
    
    /**
     * 특정 공연장의 모든 좌석을 배치 순서로 조회
     * @param venueId 공연장 ID
     * @return 해당 공연장의 좌석 목록 (구역, 열, 번호 순 정렬)
     */
    @Query("SELECT vs FROM VenueSeat vs WHERE vs.venue.id = :venueId ORDER BY vs.section, vs.seatRow, vs.seatNumber")
    List<VenueSeat> findByVenueIdOrderBySectionAndRowAndNumber(@Param("venueId") Long venueId);
    
    /**
     * 특정 공연장의 특정 등급 좌석 수 조회
     * @param venueId 공연장 ID
     * @param grade 좌석 등급
     * @return 해당 조건의 좌석 수
     */
    @Query("SELECT COUNT(vs) FROM VenueSeat vs WHERE vs.venue.id = :venueId AND vs.grade = :grade")
    long countByVenueIdAndGrade(@Param("venueId") Long venueId, @Param("grade") String grade);
}