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

    HabitEvent passedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_list);

        getSupportActionBar().setTitle("Habit Events");

        habitEventListView = findViewById(R.id.lv_habit_event);

        habitEventList = new ArrayList<>();

//----------------------------------For Test only -----------------------------------------------
        String [] habitNames = {"Habit 1", "Habit 2", "Habit 3"};
        String [] comments = {"Comment 1", "Comment 2", "Comment 3"};
        String [] locations = {"location 1", "location 2", "location 3"};

        for (int i = 0; i < 3; i++) {
            habitEventList.add(new HabitEvent(habitNames[i], comments[i], locations[i], null));
        }
//---------------------------------For Test only -----------------------------------------------

//--------------------------------------------- Process List View-----------------------------------------------------------------------------------------------------
        habitEventAdapter = new HabitEventListAdapter(this, habitEventList);
        habitEventListView.setAdapter(habitEventAdapter); // Sets the adapter for event list, used for showing list items

        habitEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToEventEditActivity(i); // TODO: Later we need to find ways to pass habit event objects to the edit activity here
            }
        });


//---------------------------------------- Process Navigation Bar-----------------------------------------------------------------------------------------------------
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

    /**
     * Here are the steps we should take everytime we return to this activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Get passed-in data-----------------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String startMode = data.getString("StartMode");

        // This is used to enter this activity without editing the list
        // in another word: since data won;t be passed if we just want to enter this activity and see contents, we use a StartMode to identify different entry method
        // we only fetch data when it's needed
        if (!(startMode.equals("normal"))) {
            int eventIndexInList = data.getInt("EventIndex");
            passedEvent = (HabitEvent) data.getParcelable("HabitEventFromEdit");

            if (eventIndexInList >= 0) {
                // update existing entry
                HabitEvent tempEvent = habitEventAdapter.getItem(eventIndexInList);
                tempEvent.setComment(passedEvent.getComment());
                tempEvent.setLocation(passedEvent.getLocation());
                tempEvent.setImageFilePath(passedEvent.getImageFilePath());
            }
            else {
                // add new entry to list
                habitEventAdapter.add(passedEvent);
            }
            habitEventAdapter.notifyDataSetChanged();
        }

    }

    // This method is used to ensure the getIntent() method always returns the latest intent
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void goToEventEditActivity(int index) {
        Intent intent = new Intent(this, HabitEventEditActivity.class);
        intent.putExtra("HabitEventForEdit", habitEventAdapter.getItem(index));
        intent.putExtra("EventIndex", index);
        startActivity(intent);
    }


}