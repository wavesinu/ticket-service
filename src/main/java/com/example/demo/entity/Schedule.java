package com.example.demo.entity;

import com.example.demo.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"event", "ticketPrices", "tickets"})
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "show_datetime", nullable = false)
    private LocalDateTime showDatetime;

    @Column(name = "sale_open_datetime", nullable = false)
    private LocalDateTime saleOpenDatetime;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TicketPrice> ticketPrices = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    // 생성자
    public Schedule(Event event, LocalDateTime showDatetime, LocalDateTime saleOpenDatetime) {
        this.event = event;
        this.showDatetime = showDatetime;
        this.saleOpenDatetime = saleOpenDatetime;
    }

    // 비즈니스 메서드
    public void updateScheduleInfo(LocalDateTime showDatetime, LocalDateTime saleOpenDatetime) {
        this.showDatetime = showDatetime;
        this.saleOpenDatetime = saleOpenDatetime;
    }

    public void addTicketPrice(TicketPrice ticketPrice) {
        this.ticketPrices.add(ticketPrice);
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public boolean isSaleOpen() {
        return LocalDateTime.now().isAfter(saleOpenDatetime);
    }

    public boolean isEventFinished() {
        return LocalDateTime.now().isAfter(showDatetime);
    }
} 