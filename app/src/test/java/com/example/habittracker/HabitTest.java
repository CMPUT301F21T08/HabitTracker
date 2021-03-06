package com.example.habittracker;


import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class HabitTest {
    private Habit mockHabit(){
        ArrayList<Integer> occurrenceDay = new ArrayList<Integer>();
        occurrenceDay.add(1);
        occurrenceDay.add(2);
        occurrenceDay.add(3);
        String uniqueID = UUID.randomUUID().toString();
        return new Habit("Reading", "Good", "reading books three times per week", "2021-11-02", 3, "per week", occurrenceDay, uniqueID, 0, false);
    }

    /**
     * Test getter for title
     */
    @Test
    void testGetHabitTitle(){
        Habit habit = mockHabit();
        assertEquals("Reading", habit.getHabitTitle());
    }

    /**
     * Test getter for reason
     */
    @Test
    void testGetHabitReason(){
        Habit habit = mockHabit();
        assertEquals("Good", habit.getHabitReason());
    }

    /**
     * Test getter for content
     */
    @Test
    void testGetHabitContent(){
        Habit habit = mockHabit();
        assertEquals("reading books three times per week", habit.getHabitContent());
    }

    /**
     * Test getter for startDate
     */
    @Test
    void testGetStartDate(){
        Habit habit = mockHabit();
        assertEquals("2021-11-02", habit.getStartDate());
    }

    /**
     * Test getter for frequency
     */
    @Test
    void testGetFrequency(){
        Habit habit = mockHabit();
        assertEquals(3, habit.getFrequency());
    }

    /**
     * Test getter for frequencyType
     */
    @Test
    void testGetFrequencyType(){
        Habit habit = mockHabit();
        assertEquals("per week", habit.getFrequencyType());
    }

    /**
     * Test getter for occurrenceDay
     */
    @Test
    void testGetOccurrenceDay(){
        Habit habit = mockHabit();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(list, habit.getOccurrenceDay());
    }

    /**
     * Test getter for index
     */
    @Test
    void testGetIndex(){
        Habit habit = mockHabit();
        assertEquals(0,habit.getIndex());
    }
    /**
     * Test setter for index
     */
    @Test
    void testSetIndex(){
        Habit habit = mockHabit();
        int num = 0;
        habit.setIndex(num);
        assertEquals(num,habit.getIndex());
    }

    /**
     * Test setter for title
     */
    @Test
    void testSetHabitTitle(){
        Habit habit = mockHabit();
        String text = "Running";
        habit.setHabitTitle(text);
        assertEquals(text, habit.getHabitTitle());
    }

    /**
     * Test setter for content
     */
    @Test
    void testSetHabitContent(){
        Habit habit = mockHabit();
        String text = "reading books";
        habit.setHabitContent(text);
        assertEquals(text, habit.getHabitContent());
    }

    /**
     * Test setter for reason
     */
    @Test
    void testSetHabitReason(){
        Habit habit = mockHabit();
        String text = "learning";
        habit.setHabitReason(text);
        assertEquals(text, habit.getHabitReason());
    }

    /**
     * Test setter for startDate
     */
    @Test
    void testSetStartDate(){
        Habit habit = mockHabit();
        String text = "2021-11-01";
        habit.setStartDate(text);
        assertEquals(text, habit.getStartDate());
    }

    /**
     * Test setter for frequency
     */
    @Test
    void testSetFrequency(){
        Habit habit = mockHabit();
        int num = 2;
        habit.setFrequency(num);
        assertEquals(num, habit.getFrequency());
    }

    /**
     * Test setter for frequency type
     */
    @Test
    void testSetFrequencyType(){
        Habit habit = mockHabit();
        String text = "per day";
        habit.setFrequencyType(text);
        assertEquals(text, habit.getFrequencyType());
    }

    /**
     * Test setter for occurrenceDay
     */
    @Test
    void testSetOccurrenceDay(){
        Habit habit = mockHabit();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        habit.setOccurrenceDay(list);
        assertEquals(list, habit.getOccurrenceDay());
    }

    /**
     * Test getter for notDone
     */
    @Test
    void testIsNotDone(){
        Habit habit = mockHabit();
        assertEquals(true, habit.isNotDone());
    }

    /**
     * Test getter for number_of_completion
     */
    @Test
    void testGetNumber_of_completion(){
        Habit habit = mockHabit();
        assertEquals(0, habit.getNumber_of_completion());
    }

    /**
     * Test getter for lastDate
     */
    @Test
    void testGetLastDate(){
        Habit habit = mockHabit();
        assertEquals(true, habit.getLastDate().equals("empty"));
    }

    /**
     * Test getter for doneTime
     */
    @Test
    void testGetDoneTime(){
        Habit habit = mockHabit();
        assertEquals(0, habit.getDoneTime());
    }


    /**
     * Test setter for eventList
     */
    @Test
    void testSetEventList(){
        Habit habit = mockHabit();
        ArrayList<String> list = new ArrayList<>();
        list.add("event 1");
        habit.setEventList(list);
        assertEquals(true, habit.getEventList().get(0).equals("event 1"));
    }

    /**
     * Test getter for eventList
     */
    @Test
    void testGetEventList(){
        Habit habit = mockHabit();
        ArrayList<String> list = new ArrayList<>();
        list.add("event 2");
        habit.setEventList(list);
        assertEquals(true, habit.getEventList().get(0).equals("event 2"));
    }

    /**
     * Test addEvent()
     */
    @Test
    void testAddEvent(){
        Habit habit = mockHabit();
        ArrayList<String> list = new ArrayList<>();
        list.add("event 2");
        habit.setEventList(list);
        assertEquals(1, habit.getEventList().size());
        habit.addEvent("event 2");
        assertEquals(2, habit.getEventList().size());
    }


    /**
     * Test setter for lastDate
     */
    @Test
    void testLastDate(){
        Habit habit = mockHabit();
        habit.setLastDate("11-02");
        assertEquals(true, habit.getLastDate().equals("11-02"));
    }

    /**
     * Test setter for notDone
     */
    @Test
    void testNotDone(){
        Habit habit = mockHabit();
        habit.setNotDone(false);
        assertEquals(false, habit.isNotDone());
    }

    /**
     * Test refresh()
     */
    @Test
    void testRefresh(){
        Habit habit = mockHabit();
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        String currentDate = String.valueOf(month) + "-" + String.valueOf(day_of_month);
        habit.setDoneTime();
        habit.setNotDone(false);
        habit.setLastDate("11-02");
        habit.refresh(currentDate);
        assertEquals(true, habit.isNotDone());
        assertEquals(0, habit.getDoneTime());
        assertEquals(currentDate, habit.getLastDate());
    }

    /**
     * Test setDone()
     */
    @Test
    void testSetDoneTime(){
        ArrayList<Integer> occurrenceDay = new ArrayList<Integer>();
        String uniqueID = UUID.randomUUID().toString();
        occurrenceDay.add(1);
        occurrenceDay.add(2);
        occurrenceDay.add(3);
        // create instance of per week or per month habit to test setDoneTime
        Habit habit = new Habit("Reading", "Good", "reading books three times per week", "2021-11-02", 3, "per week", occurrenceDay, uniqueID, 0, false);
        assertEquals(true, habit.setDoneTime());
        assertEquals(1, habit.getDoneTime());
        assertEquals(1, habit.getNumber_of_completion());
        assertEquals(false, habit.isNotDone());
        habit = new Habit("Reading", "Good", "reading books three times per week", "2021-11-02", 3, "per month", occurrenceDay, uniqueID, 0, false);
        assertEquals(true, habit.setDoneTime());
        assertEquals(1, habit.getDoneTime());
        assertEquals(1, habit.getNumber_of_completion());
        assertEquals(false, habit.isNotDone());

        // test the setDoneTime() when the frequency of per day habit is 1
        occurrenceDay.clear();
        occurrenceDay.add(-1);
        habit = new Habit("Reading", "Good", "reading books three times per week", "2021-11-02", 1, "per day", occurrenceDay, uniqueID, 0, false);
        assertEquals(true, habit.setDoneTime());
        assertEquals(1, habit.getDoneTime());
        assertEquals(1, habit.getNumber_of_completion());
        assertEquals(false, habit.isNotDone());

        // test the setDoneTime() when the frequency of per day habit is larger than 1
        habit = new Habit("Reading", "Good", "reading books three times per week", "2021-11-02", 2, "per day", occurrenceDay, uniqueID, 0, false);
        assertEquals(false, habit.setDoneTime());
        assertEquals(1, habit.getDoneTime());
        assertEquals(1, habit.getNumber_of_completion());
        assertEquals(true, habit.isNotDone());

        assertEquals(true, habit.setDoneTime());
        assertEquals(2, habit.getDoneTime());
        assertEquals(2, habit.getNumber_of_completion());
        assertEquals(false, habit.isNotDone());
    }


    /**
     * Test calculateTimes()
     */
    @Test
    void calculateTimes(){
        ArrayList<Integer> occurrenceDay = new ArrayList<Integer>();
        String uniqueID = UUID.randomUUID().toString();
        occurrenceDay.add(1);
        occurrenceDay.add(2);
        occurrenceDay.add(3);
        // create instance of per week or per month habit to test calculateTimes
        Habit habit = new Habit("Reading", "Good", "reading books three times per week", "2021-11-02", 3, "per week", occurrenceDay, uniqueID, 0, false);
        assertEquals("2021-11-02", habit.getRecordDate());
        habit.refresh("2021-11-03");
        assertEquals("2021-11-03", habit.getLastDate());

        assertEquals(1, habit.getNeedCompletion());
        //assertEquals(1, habit.getNumber_of_completion());


        habit = new Habit("Reading", "Good", "reading books three times per month", "2021-11-02", 1, "per month", occurrenceDay, uniqueID, 0, false);
        assertEquals("2021-11-02", habit.getRecordDate());
        habit.refresh("2021-12-02");
        assertEquals("2021-12-02", habit.getLastDate());
        assertEquals(1,habit.getNeedCompletion());
        //assertEquals(1, habit.getNumber_of_completion());

        // test the calculateTimes() when the frequency is per day habit
        habit = new Habit("Reading", "Good", "reading books three times per day", "2021-11-02", 3, "per day", occurrenceDay, uniqueID, 0, false);
        assertEquals("2021-11-02", habit.getRecordDate());
        habit.refresh("2021-11-03");
        assertEquals("2021-11-03", habit.getLastDate());
        assertEquals(6,habit.getNeedCompletion());
        //assertEquals(1, habit.getNumber_of_completion());

    }

    /**
     * Test reset()
     */
    @Test
    void reset(){
        ArrayList<Integer> occurrenceDay= new ArrayList<Integer>();
        ArrayList<Integer> occurrenceDayW = new ArrayList<Integer>();
        ArrayList<Integer> occurrenceDayM = new ArrayList<Integer>();
        String uniqueID = UUID.randomUUID().toString();
        occurrenceDay.add(1);
        occurrenceDay.add(2);
        occurrenceDay.add(3);
        occurrenceDayW.add(3);
        occurrenceDayM.add(2);

        //create instance of per day to test reset
        Habit habit = new Habit("Reading", "Good", "reading books three times per day", "2021-11-02", 3, "per day", occurrenceDay, uniqueID, 0, false);
        assertEquals("2021-11-02", habit.getRecordDate());
        habit.refresh("2021-11-03");
        assertEquals("2021-11-03", habit.getLastDate());
        assertEquals(6,habit.getNeedCompletion());
        habit.reset();
        assertEquals(3,habit.getNeedCompletion());

        //create instance of per week to test reset
        habit = new Habit("Reading", "Good", "reading books three times per week", "2021-11-02", 1, "per week", occurrenceDayW, uniqueID, 0, false);
        habit.refresh("2021-11-02");
        assertEquals("2021-11-03", habit.getRecordDate());
        assertEquals("2021-11-02", habit.getLastDate());

        assertEquals(1, habit.getNeedCompletion());
        habit.reset();
        assertEquals(0,habit.getNeedCompletion());


        //create instance of per month to test reset
        habit = new Habit("Reading", "Good", "reading books three times per month", "2021-11-02", 1, "per month", occurrenceDayM, uniqueID, 0, false);
        habit.refresh("2021-11-02");
        assertEquals("2021-11-03", habit.getRecordDate());
        //habit.refresh("2021-11-02");
        assertEquals("2021-11-02", habit.getLastDate());

        assertEquals(1, habit.getNeedCompletion());
        habit.reset();
        assertEquals(0,habit.getNeedCompletion());




    }


}
