package com.example.habittracker;

import java.io.Serializable;
import java.util.ArrayList;

public class Habit implements Serializable {
    private String habitTitle;
    private String habitReason;
    private String habitContent;
    private String startDate;
    private String lastDate;
    private int frequency;
    private String frequencyType;
    private ArrayList<Integer> occurrenceDay;
    private boolean notDone;
    private int number_of_completion;
    private int doneTime;

    public Habit(String habitTitle, String habitReason, String habitContent, String startDate, int frequency, String frequencyType, ArrayList<Integer> occurrenceDay){
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.habitContent = habitContent;
        this.startDate = startDate;
        this.frequency = frequency;
        this.frequencyType = frequencyType;
        this.occurrenceDay = occurrenceDay;
        this.number_of_completion = 0;
        this.notDone = true;
        this.doneTime = 0;
        this.lastDate = "null";
    }

    public Habit(){
    }

    public void refresh(String currentDate){
        if(!currentDate.equals(this.lastDate)){
            this.notDone = true;
            this.doneTime = 0;
            this.lastDate = currentDate;
        }
    }

    public boolean setDoneTime(){
        if(this.frequencyType.equals("per week") || this.frequencyType.equals("per month")){
            doneTime++;
            number_of_completion++;
            notDone = false;
            return true;
        } else {
            if(this.frequency == 1){
                doneTime++;
                number_of_completion++;
                notDone = false;
                return true;
            } else {
                doneTime++;
                number_of_completion++;
                if(doneTime == this.frequency){
                    notDone = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
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
        if(frequencyType.equals("per week") || frequencyType.equals("per month")){
            if(this.doneTime >= 1){
                this.notDone = false;
            }
        } else {
            if(this.doneTime >= this.frequency){
                this.notDone = false;
            } else {
                this.notDone = true;
            }
        }
        this.frequencyType = frequencyType;
    }

    public ArrayList<Integer> getOccurrenceDay() {
        return occurrenceDay;
    }

    public void setOccurrenceDay(ArrayList<Integer> occurrenceDay) {
        this.occurrenceDay = occurrenceDay;
    }

    public boolean isNotDone() {
        return notDone;
    }

    public int getNumber_of_completion() {
        return number_of_completion;
    }

    public String getLastDate() {
        return lastDate;
    }

    public int getDoneTime() {
        return doneTime;
    }
}