package com.example.demo.entity;

import com.example.demo.entity.common.BaseEntity;
import com.example.demo.entity.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets", indexes = {
    @Index(name = "idx_ticket_schedule_status", columnList = "schedule_id, status")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"schedule", "venueSeat", "reservation"})
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_seat_id", nullable = false)
    private VenueSeat venueSeat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "price", nullable = false, precision = 10, scale = 0)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TicketStatus status;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Version
    @Column(name = "version")
    private Long version;

    // 생성자
    public Ticket(Schedule schedule, VenueSeat venueSeat, BigDecimal price) {
        this.schedule = schedule;
        this.venueSeat = venueSeat;
        this.price = price;
        this.status = TicketStatus.AVAILABLE;
    }

    // 비즈니스 메서드
    public void reserve(Reservation reservation, int reservationMinutes) {
        this.status = TicketStatus.RESERVED;
        this.reservation = reservation;
        this.reservedAt = LocalDateTime.now();
        this.expiresAt = this.reservedAt.plusMinutes(reservationMinutes);
    }

    public void sell() {
        this.status = TicketStatus.SOLD;
        this.expiresAt = null;
    }

    public void cancel() {
        this.status = TicketStatus.CANCELLED;
        this.reservation = null;
        this.reservedAt = null;
        this.expiresAt = null;
    }

    public void releaseReservation() {
        this.status = TicketStatus.AVAILABLE;
        this.reservation = null;
        this.reservedAt = null;
        this.expiresAt = null;
    }

    public boolean isAvailable() {
        return this.status == TicketStatus.AVAILABLE;
    }

    public boolean isReserved() {
        return this.status == TicketStatus.RESERVED;
    }

    public boolean isSold() {
        return this.status == TicketStatus.SOLD;
    }

    public boolean isExpired() {
        return this.expiresAt != null && LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean canBeCancelled() {
        return this.status == TicketStatus.RESERVED || this.status == TicketStatus.SOLD;
    }
} 