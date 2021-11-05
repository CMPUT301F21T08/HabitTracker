/**
 * @author 'yhu19'
 * A class that represents the habits of users.
 *
 */

package com.example.habittracker;

import java.io.Serializable;
import java.util.ArrayList;

public class Habit implements Serializable {
// attributes of the habit class
    // title of the habit
    private String habitTitle;
    // reason of the habit
    private String habitReason;
    // content of the habit
    private String habitContent;
    // start date of the habit
    private String startDate;
    // a
    private String lastDate;
    // frequency of the habit
    private int frequency;
    // frequency type of the habit
    private String frequencyType;
    // an arrayList that stores the occurrence days of the habit
    private ArrayList<Integer> occurrenceDay;
    // an attribute used to denote whether the habit has been done at specific day
    private boolean notDone;
    // an attribute used to store the total amount of completion of the habit
    private int number_of_completion;
    // an attribute used to store the done time of habit at a specific day
    private int doneTime;
    // an arrayList that stores all the habit events related to the habit
    private ArrayList<String> eventList = new ArrayList<>();



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

    public ArrayList<String> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<String> eventList) {
        this.eventList = eventList;
    }

    public void addEvent(String eventName) {
        this.eventList.add(eventName);
    }

}