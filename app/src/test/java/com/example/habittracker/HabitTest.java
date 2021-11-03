package com.example.habittracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


public class HabitTest {
    private Habit mockHabit(){
        ArrayList<Integer> occurrenceDay = new ArrayList<Integer>();
        occurrenceDay.add(1);
        occurrenceDay.add(2);
        occurrenceDay.add(3);
        return new Habit("Reading", "Good", "reading books three times per week", "2021-11-02", 3, "per week", occurrenceDay);
    }

    @Test
    void testGetHabitTitle(){
        Habit habit = mockHabit();
        assertEquals("Reading", habit.getHabitTitle());
    }

    @Test
    void testGetHabitReason(){
        Habit habit = mockHabit();
        assertEquals("Good", habit.getHabitReason());
    }

    @Test
    void testGetHabitContent(){
        Habit habit = mockHabit();
        assertEquals("reading books three times per week", habit.getHabitContent());
    }

    @Test
    void testGetStartDate(){
        Habit habit = mockHabit();
        assertEquals("2021-11-02", habit.getStartDate());
    }

    @Test
    void testGetFrequency(){
        Habit habit = mockHabit();
        assertEquals(3, habit.getFrequency());
    }

    @Test
    void testGetFrequencyType(){
        Habit habit = mockHabit();
        assertEquals("per week", habit.getFrequencyType());
    }

    @Test
    void testGetOccurrenceDay(){
        Habit habit = mockHabit();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(list, habit.getOccurrenceDay());
    }

    @Test
    void testSetHabitTitle(){
        Habit habit = mockHabit();
        String text = "Running";
        habit.setHabitTitle(text);
        assertEquals(text, habit.getHabitTitle());
    }

    @Test
    void testSetHabitContent(){
        Habit habit = mockHabit();
        String text = "reading books";
        habit.setHabitContent(text);
        assertEquals(text, habit.getHabitContent());
    }

    @Test
    void testSetHabitReason(){
        Habit habit = mockHabit();
        String text = "learning";
        habit.setHabitReason(text);
        assertEquals(text, habit.getHabitReason());
    }

    @Test
    void testSetStartDate(){
        Habit habit = mockHabit();
        String text = "2021-11-01";
        habit.setStartDate(text);
        assertEquals(text, habit.getStartDate());
    }

    @Test
    void testSetFrequency(){
        Habit habit = mockHabit();
        int num = 2;
        habit.setFrequency(num);
        assertEquals(num, habit.getFrequency());
    }

    @Test
    void testSetFrequencyType(){
        Habit habit = mockHabit();
        String text = "per day";
        habit.setFrequencyType(text);
        assertEquals(text, habit.getFrequencyType());
    }

    @Test
    void testSetOccurrenceDay(){
        Habit habit = mockHabit();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        habit.setOccurrenceDay(list);
        assertEquals(list, habit.getOccurrenceDay());
    }
}
