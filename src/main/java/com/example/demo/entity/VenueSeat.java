package com.example.demo.entity;

import com.example.demo.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venue_seats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"venue", "tickets"})
public class VenueSeat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(name = "section", nullable = false, length = 50)
    private String section;

    @Column(name = "seat_row", nullable = false, length = 10)
    private String seatRow;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "grade", nullable = false, length = 20)
    private String grade;

    @OneToMany(mappedBy = "venueSeat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    // 생성자
    public VenueSeat(Venue venue, String section, String seatRow, 
                     String seatNumber, String grade) {
        this.venue = venue;
        this.section = section;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.grade = grade;
    }

    // 비즈니스 메서드
    public void updateSeatInfo(String section, String seatRow, 
                              String seatNumber, String grade) {
        this.section = section;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.grade = grade;
    }

    public String getFullSeatName() {
        return section + " " + seatRow + "열 " + seatNumber + "번";
    }
} 