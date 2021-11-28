package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitListAdapter;

import junit.framework.TestCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class HabitListClickListenerTest extends TestCase {
    Habit testHabit;
    //ListView habitListView;
    RecyclerView habitRecycleView;
    ArrayList<Habit> habitArrayList;
    HabitListAdapter habitListAdapter;
    Context context;
    Activity activity;
    String uid;

    @BeforeEach
    public void constructHabit() {
        testHabit = new Habit("Reading", "Good", "reading books three times per day", "2021-11-02", 3, "per day", occurrenceDay, uniqueID, 0, false);

        habitListAdapter = new HabitListAdapter(activity.getApplicationContext(), habitArrayList);
        habitArrayList.add(testHabit);
    }

        @Test
        void testOnItemClick(){


            AdapterView.OnItemClickListener habitListClickListener = new HabitListClickListener(activity.getApplicationContext(),context,habitArrayList, uid);
            habitRecycleView.setAdapter(habitListAdapter);
            habitListAdapter.setOnItemClickListener(habitListClickListener);

        }
    }

