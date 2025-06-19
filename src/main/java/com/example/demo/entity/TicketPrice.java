package com.example.demo.entity;

import com.example.demo.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_prices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"schedule"})
public class TicketPrice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "grade", nullable = false, length = 20)
    private String grade;

    @Column(name = "price", nullable = false, precision = 10, scale = 0)
    private BigDecimal price;

    // 생성자
    public TicketPrice(Schedule schedule, String grade, BigDecimal price) {
        this.schedule = schedule;
        this.grade = grade;
        this.price = price;
    }

    // 비즈니스 메서드
    public void updatePrice(BigDecimal newPrice) {
        this.price = newPrice;
    }
} 