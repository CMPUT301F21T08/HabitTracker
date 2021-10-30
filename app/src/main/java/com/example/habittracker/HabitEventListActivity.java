package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class HabitEventListActivity extends AppCompatActivity {

    ListView habitEventListView;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventList;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_list);

        getSupportActionBar().setTitle("Habit Events");

        habitEventListView = findViewById(R.id.lv_habit_event);

        habitEventList = new ArrayList<>();

        // Test only -----------------------------------------------
        String [] titles = {"Event 1", "Event 2", "Event 3"};
        String [] comments = {"Comment 1", "Comment 2", "Comment 3"};

        for (int i = 0; i < 3; i++) {
            habitEventList.add(new HabitEvent(titles[i], comments[i]));
        }
        // Test only -----------------------------------------------

        habitEventAdapter = new HabitEventListAdapter(this, habitEventList);
        habitEventListView.setAdapter(habitEventAdapter); // Sets the adapter for event list, used for showing list items

        habitEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToEventEditActivity(); // TODO: Later we need to find ways to pass habit event objects to the edit activity here
            }
        });

        // Process Navigation Bar-----------------------------------------------------------------------------------------------------
        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_habitEvent);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_habit:
                        Intent intent1 = new Intent(HabitEventListActivity.this, HabitListActivity.class);
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.navigation_homePage:
                        Intent intent2 = new Intent(HabitEventListActivity.this, ToDoActivity.class);
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.navigation_following:
                        Intent intent3 = new Intent(HabitEventListActivity.this, FollowingActivity.class);
                        startActivity(intent3);
                        finish();
                        return true;
                    case R.id.navigation_settings:
                        Intent intent4 = new Intent(HabitEventListActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    public void goToEventEditActivity() {
        Intent intent = new Intent(this, HabitEventEditActivity.class);
        startActivity(intent);
    }


}