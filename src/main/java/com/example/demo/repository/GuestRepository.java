package com.example.demo.repository;

import com.example.demo.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 게스트 사용자 레포지토리
 * 비회원(게스트) 사용자의 데이터 액세스를 담당
 */
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    
    /**
     * 이름과 전화번호로 게스트 사용자 조회
     * @param name 게스트 이름
     * @param phoneNumber 게스트 전화번호
     * @return 조회된 게스트 사용자 (Optional)
     */
    Optional<Guest> findByNameAndPhoneNumber(String name, String phoneNumber);
    
    /**
     * 이름과 전화번호 조합의 존재 여부 확인
     * @param name 게스트 이름
     * @param phoneNumber 게스트 전화번호
     * @return 존재 여부 (true/false)
     */
    boolean existsByNameAndPhoneNumber(String name, String phoneNumber);
}