package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HabitEventListActivity extends AppCompatActivity {

    ListView habitEventListView;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_list);

        habitEventListView = findViewById(R.id.lv_habit_event);

        habitEventList = new ArrayList<>();

        // Test only -----------------------------------------------
        String [] titles = {"Event 1", "Event 2", "Event 3"};

        for (int i = 0; i < 3; i++) {
            habitEventList.add(new HabitEvent(titles[i]));
        }
        // Test only -----------------------------------------------

        habitEventAdapter = new HabitEventListAdapter(this, habitEventList);
        habitEventListView.setAdapter(habitEventAdapter); // Sets the adapter for event list, used for showing list items
    }
}