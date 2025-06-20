package com.example.demo.entity.enums;

public enum EventCategory {
    CONCERT("콘서트"),
    MUSICAL("뮤지컬"),
    THEATER("연극"),
    EXHIBITION("전시"),
    FESTIVAL("페스티벌"),
    SPORTS("스포츠"),
    LECTURE("강연"),
    OTHER("기타");

    private final String displayName;

    EventCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}