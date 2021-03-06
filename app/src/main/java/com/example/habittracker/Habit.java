/**
 * @author 'yhu19'
 * A class that represents the habits of users.
 *
 */

package com.example.habittracker;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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
    // attribute that used to determine whether the date time has changed(usually store the date of the current time)
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
    // Unique ID for each habit
    private String UUID;
    // attribute that indicates whether the habit is public or private
    private boolean publicHabit;
    // attribute the stores the number of times that the habit need to be done
    private int needCompletion = 0;
    // attribute used to order the habit in the habit list
    private int index;
    // the starting date used in calculating the total number of need completion
    private String recordDate;


    /**
     * Habit Constructor
     *
     * @param habitTitle    title of the habit
     * @param habitReason   reason of the habit
     * @param habitContent  content of the habit
     * @param startDate     the starting date of the habit
     * @param frequency     the frequency of the habit
     * @param frequencyType the frequency type of the habit
     * @param occurrenceDay the occurrence day of the habit
     * @param uuid          the id for the habit
     * @param index         the index for the habit in the habit list
     * @param publicHabit   disclosure status of the habit
     */
    public Habit(String habitTitle, String habitReason, String habitContent, String startDate, int frequency, String frequencyType, ArrayList<Integer> occurrenceDay, String uuid, int index, boolean publicHabit) {
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.habitContent = habitContent;
        this.startDate = startDate;
        this.recordDate = startDate;
        this.frequency = frequency;
        this.frequencyType = frequencyType;
        this.occurrenceDay = occurrenceDay;
        this.number_of_completion = 0;
        this.notDone = true;
        this.doneTime = 0;
        this.lastDate = "empty";
        this.UUID = uuid;
        this.index = index;
        this.publicHabit = publicHabit;
    }

    /**
     * Null constructor (never used, only for database)
     */
    public Habit() {
    }

    /**
     * This method will take the current date time as parameter to compare with the date that have already been store
     * in the lastDate attribute to determine whether the current date has ended or not. And it will refresh the
     * notDone and doneTime attribute when the current date has ended.
     *
     * @param currentDate
     */
    public void refresh(String currentDate) {
        // if this two date is different, we will refresh the attributes
        if (!currentDate.equals(this.lastDate)) {
            this.notDone = true;
            this.doneTime = 0;
            this.lastDate = currentDate;
            calculateTimes();
        }

    }


    /**
     * This method will be invoked when the habit is being checked in the to do list(main page).This method will return
     * true to denote that the habit has been done today; and will return false to denote that the habit has not yet been
     * finished today.
     */
    public boolean setDoneTime() {
        // "per week" and "per month" frequency only need to be done one time per day, so it will return true when this function
        // is called
        if (this.frequencyType.equals("per week") || this.frequencyType.equals("per month")) {
            doneTime++;
            number_of_completion++;
            notDone = false;
            return true;
            // for the "per day" frequency habit, we need to compare the done time with the frequency to determine whether it is finished
            // or not
        } else {
            // if the frequency is 1, then it will return true since only need to be done for one time
            if (this.frequency == 1) {
                doneTime++;
                number_of_completion++;
                notDone = false;
                return true;
            } else {
                doneTime++;
                number_of_completion++;
                // compare the doneTime with the frequency
                if (doneTime == this.frequency) {
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
     *
     * @return
     */
    public String getHabitTitle() {
        return habitTitle;
    }

    /**
     * Setter for habit title
     *
     * @param habitTitle
     */
    public void setHabitTitle(String habitTitle) {
        this.habitTitle = habitTitle;
    }

    /**
     * Getter for habit reason
     *
     * @return
     */
    public String getHabitReason() {
        return habitReason;
    }

    /**
     * Setter for habit reason
     *
     * @param habitReason
     */
    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }

    /**
     * Getter for habit content
     *
     * @return
     */
    public String getHabitContent() {
        return habitContent;
    }

    /**
     * Setter for habit content
     *
     * @param habitContent
     */
    public void setHabitContent(String habitContent) {
        this.habitContent = habitContent;
    }

    /**
     * Getter for habit starting date
     *
     * @return
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Setter for starting date of habit
     *
     * @param startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for habit frequency
     *
     * @return
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Setter for habit frequency
     *
     * @param frequency
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Getter for habit frequency type
     *
     * @return
     */
    public String getFrequencyType() {
        return frequencyType;
    }

    /**
     * Setter for habit frequency type
     *
     * @param frequencyType
     */
    public void setFrequencyType(String frequencyType) {
        // this if statement will update the notDone attribute when user modifies the frequency and frequency type of the habit
        // when the new frequency type is "per week" or "per month", if user have already done more than once for the habit,
        // then set the habit as done since these two types of frequency can only occur one time per day
        if (frequencyType.equals("per week") || frequencyType.equals("per month")) {
            if (this.doneTime >= 1) {
                this.notDone = false;
            }
        } else {
            // if the new frequency type is "per day", then comparing the doneTime and the frequency to determine whether the
            // habit is done today
            if (this.doneTime >= this.frequency) {
                this.notDone = false;
            } else {
                this.notDone = true;
            }
        }
        this.frequencyType = frequencyType;
    }

    /**
     * Getter for habit occurrence days
     *
     * @return
     */
    public ArrayList<Integer> getOccurrenceDay() {
        return occurrenceDay;
    }

    /**
     * Setter for habit occurrenceDay
     *
     * @param occurrenceDay
     */
    public void setOccurrenceDay(ArrayList<Integer> occurrenceDay) {
        this.occurrenceDay = occurrenceDay;
    }

    /**
     * Getter for the notDone attribute
     *
     * @return
     */
    public boolean isNotDone() {
        return notDone;
    }

    /**
     * Getter for the total completion times of habit
     *
     * @return
     */
    public int getNumber_of_completion() {
        return number_of_completion;
    }

    /**
     * Getter for lastDate attribute of habit
     *
     * @return
     */
    public String getLastDate() {
        return lastDate;
    }

    /**
     * Getter for doneTime attribute of habit
     *
     * @return
     */
    public int getDoneTime() {
        return doneTime;
    }

    /**
     * Getter for list of habit events related to habit
     *
     * @return
     */
    public ArrayList<String> getEventList() {
        return eventList;
    }

    /**
     * Setter for list of habit events related to habit
     *
     * @param eventList
     */
    public void setEventList(ArrayList<String> eventList) {
        this.eventList = eventList;
    }

    /**
     * adding a new event to eventList attributes of habit
     *
     * @param eventName
     */
    public void addEvent(String eventName) {
        this.eventList.add(eventName);
    }


    /**
     * Setter for lastDate of habit
     *
     * @param lastDate
     */
    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    /**
     * Setter for notDone of habit
     *
     * @param notDone
     */
    public void setNotDone(boolean notDone) {
        this.notDone = notDone;
    }

    /**
     * Getter for UUID
     *
     * @return
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * Setter for UUID
     *
     * @param UUID
     */
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    /**
     * Getter for publicHabit
     *
     * @return
     */
    public boolean isPublicHabit() {
        return publicHabit;
    }

    /**
     * Setter for publicHabit
     *
     * @param publicHabit
     */
    public void setPublicHabit(boolean publicHabit) {
        this.publicHabit = publicHabit;
    }

    /**
     * Getter for needCompletion
     *
     * @return
     */
    public int getNeedCompletion() {
        return needCompletion;
    }

    /**
     * Getter for index
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setter for index
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Getter for recordDate
     *
     * @return
     */
    public String getRecordDate() {
        return recordDate;
    }

    /**
     * the method used to calculate how many times the habit needed to be done for the current time
     */
    public void calculateTimes() {
        // the simpleDateFormat used to create the Date variable
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        // calculate the times needed for the habits with per day frequency
        if (this.frequencyType.equals("per day")) {
            try {
                // create the Date variables for the record date and the current date
                Date date1 = myFormat.parse(this.recordDate);
                Date date2 = myFormat.parse(this.lastDate);
                // get the difference between two dates
                long diff = date2.getTime() - date1.getTime();
                // round up the difference into integer to reflect the amount of days
                int days = (int) (diff / (1000 * 60 * 60 * 24)) + 1;
                // calculate the number of times needed for the period between record date and current date and add it to the variable needCompletion
                this.needCompletion = this.needCompletion + days * this.frequency;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // calculate the times needed for the habit with per week frequency
        } else if (this.frequencyType.equals("per week")) {
            try {
                // create the Date variables for the record date and the current date
                Date date1 = myFormat.parse(this.recordDate);
                Date date2 = myFormat.parse(this.lastDate);
                // loop through all the weekly occurrence days to calculate the number of times that the habit need to be done
                for (int i = 0; i < this.occurrenceDay.size(); i++) {
                    // call the calculateWeekdays method to calculate the number of  a specific week day in a period
                    this.needCompletion = this.needCompletion + calculateWeekdays(date1, date2, this.occurrenceDay.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // calculate the times needed for the habit with per month frequency
        } else {
            try {
                Date date1 = myFormat.parse(this.recordDate);
                Date date2 = myFormat.parse(this.lastDate);
                // call the calculateMonthDays method to calculate the number of times that the number of the monthly occurrence date in a period
                this.needCompletion = this.needCompletion + calculateMonthDays(date1, date2, this.occurrenceDay.get(0));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // change the value in the recordDate variable to become the next date of the current date time
        try {
            this.recordDate = this.lastDate;
            Date time = myFormat.parse(this.recordDate);
            Calendar record = Calendar.getInstance();
            record.setTime(time);
            record.add(Calendar.DAY_OF_MONTH, 1);
            this.recordDate = myFormat.format(record.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * the method used to calculate the total amount of a specific week day in a given period
     *
     * @param start the starting date of the period
     * @param end   the end date of the period
     * @param i     the number that represent the week day in the week
     * @return
     */
    private int calculateWeekdays(Date start, Date end, int i) {
        // using the two Date variables to create Calendar variables for the starting date and the end date
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        // variable used to store the total number of the specific week day in the period
        int days = 0;
        // return 0 if the starting date is after the end date
        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            return days;
        }
        // a loop that used to calculate the number of occurrence of a weekday in the period
        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            // check if the the date in the startCal is the specific week day
            if (startCal.get(Calendar.DAY_OF_WEEK) == i) {
                ++days;
            }
            // move the starting day one day up
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        // return the result
        return days;
    }

    /**
     * the method used to calculate the total amount of the monthly occurrence day in a given period
     *
     * @param start the starting date of the period
     * @param end   the end date of the period
     * @param i     the number that represent the week day in the week
     * @return
     */
    private int calculateMonthDays(Date start, Date end, int i) {
        // using the two Date variables to create Calendar variables for the starting date and the end date
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        // variable used to store the total number of the specific week day in the period
        int days = 0;
        // return 0 if the starting date is after the end date
        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            return days;
        }
        // a loop that used to calculate the number of occurrence of a date of month in the period
        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            if (startCal.get(Calendar.DAY_OF_MONTH) == i) {
                ++days;
            }
            // move the starting day one day up
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        // return the result
        return days;
    }


    /**
     * the method used to reset the times in the needCompletion variable and the date in the recordDate variable
     * if the user edits habit
     */
    public void reset() {
        // set up the Calendar variable
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar current = Calendar.getInstance();
        try {
            Date time = myFormat.parse(lastDate);
            current.setTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // if the habit is with per week frequency
        if (frequencyType.equals("per week")) {
            // if the habit need to be done today
            if (this.occurrenceDay.contains(current.get(Calendar.DAY_OF_WEEK))) {
                // decrement the times in the needCompletion by 1
                this.needCompletion--;
                // set the date in the recordDate to be the current date in the lastDate
                this.recordDate = this.lastDate;
            }

        }
        // if the habit is with per month frequency
        if (frequencyType.equals("per month")) {
            // if the habit need to be done today
            if (this.occurrenceDay.contains(current.get(Calendar.DAY_OF_MONTH))) {
                // decrement the times in the needCompletion by 1
                this.needCompletion--;
                // set the date in the recordDate to be the current date in the lastDate
                this.recordDate = this.lastDate;
            }

        }
        // if the habit is with per day frequency
        if (frequencyType.equals("per day")) {
            // decrement the times in the needCompletion by the number of frequency of the habit
            this.needCompletion = this.needCompletion - this.frequency;
            // set the date in the recordDate to be the current date in the lastDate
            this.recordDate = this.lastDate;
        }
    }
}