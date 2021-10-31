package com.example.habittracker;

import java.io.Serializable;
import java.util.ArrayList;

public class Habit implements Serializable {
    private String habitTitle;
    private String habitReason;
    private String habitContent;
    private String startDate;
    private int frequency;
    private String frequencyType;
    private ArrayList<Integer> occurrenceDay;

    public Habit(String habitTitle, String habitReason, String habitContent, String startDate, int frequency, String frequencyType, ArrayList<Integer> occurrenceDay){
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.habitContent = habitContent;
        this.startDate = startDate;
        this.frequency = frequency;
        this.frequencyType = frequencyType;
        this.occurrenceDay = occurrenceDay;
    }


    public String getHabitTitle() {
        return habitTitle;
    }

    public void setHabitTitle(String habitTitle) {
        this.habitTitle = habitTitle;
    }

    public String getHabitReason() {
        return habitReason;
    }

    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }

    public String getHabitContent() {
        return habitContent;
    }

    public void setHabitContent(String habitContent) {
        this.habitContent = habitContent;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public ArrayList<Integer> getOccurrenceDay() {
        return occurrenceDay;
    }

    public void setOccurrenceDay(ArrayList<Integer> occurrenceDay) {
        this.occurrenceDay = occurrenceDay;
    }
}