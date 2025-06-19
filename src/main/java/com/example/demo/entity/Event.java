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
@Table(name = "events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"venue", "schedules"})
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "artist", nullable = false, length = 100)
    private String artist;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "age_restriction", nullable = false)
    private Integer ageRestriction;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    // 생성자
    public Event(Venue venue, String name, String artist, String description, 
                 String category, Integer ageRestriction) {
        this.venue = venue;
        this.name = name;
        this.artist = artist;
        this.description = description;
        this.category = category;
        this.ageRestriction = ageRestriction;
    }

    // 비즈니스 메서드
    public void updateEventInfo(String name, String artist, String description, 
                               String category, Integer ageRestriction) {
        this.name = name;
        this.artist = artist;
        this.description = description;
        this.category = category;
        this.ageRestriction = ageRestriction;
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }
} 