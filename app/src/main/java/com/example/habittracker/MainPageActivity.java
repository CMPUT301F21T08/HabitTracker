package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class MainPageActivity extends AppCompatActivity {
    private FirebaseAuth authentication;
    private String uid;
    private Calendar calendar = Calendar.getInstance();
    private ArrayAdapter<Habit> toDoAdapter;
    private ArrayList<Habit> toDoList;
    private Date date = new Date();
    private int weekDay;
    private int day_of_month;
    private ListView toDoListView;
    private int month;
    private String currentDate;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoListView = findViewById(R.id.habitToDo_listView);
        //get the current date information
        calendar.setTime(date);
        weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        currentDate = String.valueOf(month) + "-" + String.valueOf(day_of_month);
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }
        toDoList = new ArrayList<Habit>();
        toDoAdapter = new ToDoListAdapter(this, toDoList);
        toDoListView.setAdapter(toDoAdapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toDoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Habit habit = (Habit) dataSnapshot.getValue(Habit.class);
                    habit.refresh(currentDate);
                    ArrayList<Integer> occurrenceDay = habit.getOccurrenceDay();
                    switch (habit.getFrequencyType()){
                        case "per day":
                            if(habit.isNotDone()) {
                                toDoList.add(habit);
                            }
                            break;
                        case "per week":
                            if(occurrenceDay.contains(weekDay) && habit.isNotDone()){
                                toDoList.add(habit);
                            }
                            break;
                        case "per month":
                            if(occurrenceDay.contains(day_of_month) && habit.isNotDone()){
                                toDoList.add(habit);
                            }
                            break;
                    }
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(habit.getHabitTitle(),habit);
                    FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                }
                toDoAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read Data Failed");
            }
        });




        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_homePage);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_habit:
                        Intent intent1 = new Intent(MainPageActivity.this, HabitListActivity.class);
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.navigation_habitEvent:
                        Intent intent2 = new Intent(MainPageActivity.this, HabitEventListActivity.class);
                        intent2.putExtra("StartMode", "normal");
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.navigation_homePage:
                        return true;
                    case R.id.navigation_following:
                        Intent intent3 = new Intent(MainPageActivity.this, FollowingActivity.class);
                        startActivity(intent3);
                        finish();
                        return true;
                    case R.id.navigation_settings:
                        Intent intent4 = new Intent(MainPageActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}