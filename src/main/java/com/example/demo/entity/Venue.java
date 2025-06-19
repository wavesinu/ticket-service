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
@Table(name = "venues")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"venueSeats", "events"})
public class Venue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @OneToMany(mappedBy = "venue", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VenueSeat> venueSeats = new ArrayList<>();

    @OneToMany(mappedBy = "venue", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<>();

    // 생성자
    public Venue(String name, String address, Integer totalSeats) {
        this.name = name;
        this.address = address;
        this.totalSeats = totalSeats;
    }

    // 비즈니스 메서드
    public void updateInfo(String name, String address, Integer totalSeats) {
        this.name = name;
        this.address = address;
        this.totalSeats = totalSeats;
    }

    public void addVenueSeat(VenueSeat venueSeat) {
        this.venueSeats.add(venueSeat);
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }
}
