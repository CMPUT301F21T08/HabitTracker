package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.habittracker.HabitEvent;
import com.example.habittracker.HabitEventEditActivity;
import com.example.habittracker.HabitEventListAdapter;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class EventListClickListenerTest extends TestCase {
    HabitEvent testEvent;
    ListView habitEventListView;
    ArrayList<HabitEvent> habitEventArrayList;
    HabitEventListAdapter habitEventListAdapter;
    Context context;
    Activity activity;


    /**
     * Construct an event object to be used for test
     */
    @BeforeEach
    public void constructEvent() {
        testEvent = new HabitEvent("Run", "I've finished running", "Edmonton", "1234-5678", "1111-2222");
        HabitEventListAdapter habitEventListAdapter = new HabitEventListAdapter(activity.getApplicationContext(),habitEventArrayList);
        habitEventListAdapter.add(testEvent);
    }


    @Test
    void testOnItemClick() {


        AdapterView.OnItemClickListener habitEventListListener = new EventListClickListener(activity.getApplicationContext(),activity,habitEventListAdapter);
        habitEventListView.setAdapter(habitEventListAdapter);
        habitEventListView.setOnItemClickListener(habitEventListListener);

    }

//    void testGoToEventEditActivity() {
//    }
}