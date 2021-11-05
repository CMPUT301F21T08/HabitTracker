/**
 * @author 'yhu19'
 * A class that represents the habits of users.
 *
 */

package com.example.habittracker;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * The habit class
 * Using Serializable interface to achieve passing habit objects between activities
 */
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
    // attribute that used to determine whether the date time has changed
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


    /**
     * Habit Event Constructor
     * @param habitTitle title of the habit
     * @param habitReason reason of the habit
     * @param habitContent content of the habit
     * @param startDate the starting date of the habit
     * @param frequency the frequency of the habit
     * @param frequencyType the frequency type of the habit
     * @param occurrenceDay the occurrence day of the habit
     */
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

    /**
     * Null constructor (never used, only for database)
     */
    public Habit(){
    }

    /**
     * This method will take the current date time as parameter to compare with the date that have already been store
     * in the lastDate attribute to determine whether the current date has ended or not. And it will refresh the
     * notDone and doneTime attribute when the current date has ended.
     * @param currentDate
     */
    public void refresh(String currentDate){
        // if this two date is different, we will refresh the attributes
        if(!currentDate.equals(this.lastDate)){
            this.notDone = true;
            this.doneTime = 0;
            this.lastDate = currentDate;
        }
    }


    /**
     * This method will be invoked when the habit is being checked in the to do list(main page).This method will return
     * true to denote that the habit has been done today; and will return false to denote that the habit has not yet been
     * finished today.
     *
     */
    public boolean setDoneTime(){
        // "per week" and "per month" frequency only need to be done one time per day, so it will return true when this function
        // is called
        if(this.frequencyType.equals("per week") || this.frequencyType.equals("per month")){
            doneTime++;
            number_of_completion++;
            notDone = false;
            return true;
        // for the "per day" frequency habit, we need to compare the done time with the frequency to determine whether it is finished
        // or not
        } else {
            // if the frequency is 1, then it will return true since only need to be done for one time
            if(this.frequency == 1){
                doneTime++;
                number_of_completion++;
                notDone = false;
                return true;
            } else {
                doneTime++;
                number_of_completion++;
                // compare the doneTime with the frequency
                if(doneTime == this.frequency){
                    notDone = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * Getter for habit title
     * @return
     */
    public String getHabitTitle() {
        return habitTitle;
    }

    /**
     * Setter for habit title
     * @param habitTitle
     */
    public void setHabitTitle(String habitTitle) {
        this.habitTitle = habitTitle;
    }

    /**
     * Getter for habit reason
     * @return
     */
    public String getHabitReason() {
        return habitReason;
    }

    /**
     * Setter for habit reason
     * @param habitReason
     */
    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }

    /**
     * Getter for habit content
     * @return
     */
    public String getHabitContent() {
        return habitContent;
    }

    /**
     * Setter for habit content
     * @param habitContent
     */
    public void setHabitContent(String habitContent) {
        this.habitContent = habitContent;
    }

    /**
     * Getter for habit starting date
     * @return
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Setter for starting date of habit
     * @param startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for habit frequency
     * @return
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Setter for habit frequency
     * @param frequency
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Getter for habit frequency type
     * @return
     */
    public String getFrequencyType() {
        return frequencyType;
    }

    /**
     * Setter for habit frequency type
     * @param frequencyType
     */
    public void setFrequencyType(String frequencyType) {
    // this if statement will update the notDone attribute when user modifies the frequency and frequency type of the habit
        // when the new frequency type is "per week" or "per month", if user have already done more than once for the habit,
        // then set the habit as done since these two types of frequency can only occur one time per day
        if(frequencyType.equals("per week") || frequencyType.equals("per month")){
            if(this.doneTime >= 1){
                this.notDone = false;
            }
        } else {
            // if the new frequency type is "per day", then comparing the doneTime and the frequency to determine whether the
            // habit is done today
            if(this.doneTime >= this.frequency){
                this.notDone = false;
            } else {
                this.notDone = true;
            }
        }
        this.frequencyType = frequencyType;
    }

    /**
     * Getter for habit occurrence days
     * @return
     */
    public ArrayList<Integer> getOccurrenceDay() {
        return occurrenceDay;
    }

    /**
     * Setter for habit occurrenceDay
     * @param occurrenceDay
     */
    public void setOccurrenceDay(ArrayList<Integer> occurrenceDay) {
        this.occurrenceDay = occurrenceDay;
    }

    /**
     * Getter for the notDone attribute
     * @return
     */
    public boolean isNotDone() {
        return notDone;
    }

    /**
     * Getter for the total completion times of habit
     * @return
     */
    public int getNumber_of_completion() {
        return number_of_completion;
    }

    /**
     * Getter for lastDate attribute of habit
     * @return
     */
    public String getLastDate() {
        return lastDate;
    }

    /**
     * Getter for doneTime attribute of habit
     * @return
     */
    public int getDoneTime() {
        return doneTime;
    }

    /**
     * Getter for list of habit events related to habit
     * @return
     */
    public ArrayList<String> getEventList() {
        return eventList;
    }

    /**
     * Setter for list of habit events related to habit
     * @param eventList
     */
    public void setEventList(ArrayList<String> eventList) {
        this.eventList = eventList;
    }

    /**
     * adding a new event to eventList attributes of habit
     * @param eventName
     */
    public void addEvent(String eventName) {
        this.eventList.add(eventName);
    }


    /**
     * Setter for lastDate of habit
     * @param lastDate
     */
    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    /**
     * Setter for notDone of habit
     * @param notDone
     */
    public void setNotDone(boolean notDone) {
        this.notDone = notDone;
    }
}