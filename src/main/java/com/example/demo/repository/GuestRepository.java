package com.example.demo.repository;

import com.example.demo.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    
    Optional<Guest> findByNameAndPhoneNumber(String name, String phoneNumber);
    
    boolean existsByNameAndPhoneNumber(String name, String phoneNumber);
}