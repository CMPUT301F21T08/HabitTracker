package com.example.habittracker;

public class HabitEvent {
    private String eventTitle;

    public HabitEvent(String title) {
        this.eventTitle = title;
    }

    public String getEventTitle() {
        return eventTitle;
    }
}
