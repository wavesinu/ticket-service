package com.example.demo.entity;

import com.example.demo.entity.common.BaseEntity;
import com.example.demo.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"user", "tickets", "payment"})
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_price", nullable = false, precision = 12, scale = 0)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payment payment;

    // 생성자
    public Reservation(User user, BigDecimal totalPrice, ReservationStatus status) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // 비즈니스 메서드
    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    public void expire() {
        this.status = ReservationStatus.EXPIRED;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public boolean isPendingPayment() {
        return this.status == ReservationStatus.PENDING_PAYMENT;
    }

    public boolean isConfirmed() {
        return this.status == ReservationStatus.CONFIRMED;
    }

    public boolean isCancellable() {
        return this.status == ReservationStatus.PENDING_PAYMENT || 
               this.status == ReservationStatus.CONFIRMED;
    }
} 