package com.example.habittracker;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HabitEventListActivityTest extends TestCase {
    private HabitEventListAdapter habitEventListAdapter;
    Context context;

    HabitEvent testEvent;


    ArrayList<HabitEvent> habitEventArrayList;

    Context activity;

    /**
     * Construct an event object to be used for test
     */
    @Before
    public void constructEvent() {
        testEvent = new HabitEvent("Run", "I've finished running", "Edmonton", "1234-5678", "1111-2222");

        HabitEventListAdapter habitEventListAdapter = new HabitEventListAdapter(activity.getApplicationContext(),habitEventArrayList);
        habitEventListAdapter.add(testEvent);

    }



//    @Before
//    public void setUp() throws Exception {
//        super.setUp();
//
//    }

    public void testOnCreate() {

    }

    public void testOnResume() {
    }

    public void testOnNewIntent() {
    }
}