package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 레포지토리
 * 일반 회원가입 사용자 및 소셜 로그인 사용자의 데이터 액세스를 담당
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return 조회된 사용자 (Optional)
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 이메일 존재 여부 확인
     * @param email 확인할 이메일
     * @return 존재 여부 (true/false)
     */
    boolean existsByEmail(String email);
    
    /**
     * 이메일과 비밀번호로 사용자 조회 (일반 로그인)
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 조회된 사용자 (Optional)
     */
    Optional<User> findByEmailAndPassword(String email, String password);
    
    /**
     * 소셜 로그인 제공자와 제공자 ID로 사용자 조회
     * @param provider 소셜 로그인 제공자 (예: kakao, google)
     * @param providerId 제공자에서 발급한 사용자 ID
     * @return 조회된 사용자 (Optional)
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}