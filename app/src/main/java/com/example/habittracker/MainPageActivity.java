/**
 * @author 'yhu19'
 * Allow user to modefied the information of the existing habit or add a new habit
 *
 */
package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainPageActivity extends AppCompatActivity {
    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID
    // variable used to get the current time
    private Calendar calendar = Calendar.getInstance();
    // adapter used to set up the listView
    private ArrayAdapter<Habit> toDoAdapter;
    private ArrayList<Habit> toDoList;
    // variable used to get the current time
    private Date date = new Date();
    // variable stores the current week day information for the day
    private int weekDay;
    // variable stores the current month day information for the day
    private int day_of_month;
    private ListView toDoListView;
    // variable stores the current month using integer representation
    private int month;
    // variable stores the current day as format "MM-dd"
    private String currentDate;
    private int year;

    private int backCount;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backCount = 0;

        // set up the listView for the activity
        toDoListView = findViewById(R.id.habitToDo_listView);
        //get the current date information
        calendar.setTime(date);
        weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        // transfer the current day information into format "MM-dd"
        currentDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day_of_month);

        // firebase connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // set up the adapter with the habit list
        toDoList = new ArrayList<Habit>();
        toDoAdapter = new ToDoListAdapter(this, toDoList);
        // set up the listView with the adapter
        toDoListView.setAdapter(toDoAdapter);

        // get all the habits of the user from database and determine whether it should appear in the main activity(to do list)
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toDoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Habit habit = (Habit) dataSnapshot.getValue(Habit.class);

                    if (habit.getDoneTime() == habit.getFrequency()) {
                        habit.setNotDone(false);
                    }

                    // refresh all the information related to done time of habit if the date is changed
                    habit.refresh(currentDate);
                    // get the occurrence day of the habit
                    ArrayList<Integer> occurrenceDay = habit.getOccurrenceDay();
                    // this will distinguish three types of habit and do different operations
                    switch (habit.getFrequencyType()){
                        case "per day":
                            // check if the habit is finished/done today; if not done show on the to do list
                            if(habit.isNotDone()) {
                                toDoList.add(habit);
                            }
                            break;
                        // for the per week or per month frequency, we not only need to check whether the habit is done
                        // or finished, we also need to check its occurrence day to see whether the date of today is in the
                        // occurrence day list of the habit
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
                }
                toDoAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read Data Failed");
            }
        });

        // set up the OnItemClickListener for the listView to allow user to go to description page to see the detailed
        // information of habit
        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit tapHabit = toDoList.get(i);
                goToHabitDescriptionActivity(i, tapHabit);

            }
        });

        // Process Navigation Bar
        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_homePage);

        //NavigationBarView.OnItemSelectedListener bottomNavigationViewOnItemSelectedListener = new NavigationBarClickListener(getApplicationContext(),this);
        //bottomNavigationView.setOnItemSelectedListener(bottomNavigationViewOnItemSelectedListener);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for(int i = 0; i < toDoList.size(); i++){
                    Habit habit = toDoList.get(i);
                    // upload the information to database to update all habit
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(habit.getUUID(),habit);
                    FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                }
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

    /**
     * Process the KEY_RETURN signal in main page activity
     * When back button is pressed more than once, exit program
     */
    @Override
    public void onBackPressed() {
        backCount+=1;
        if (backCount == 1) {
            Toast.makeText(MainPageActivity.this, "Press the back button again to return to home page", Toast.LENGTH_SHORT).show();
        }
        else {
            backCount = 0;
            super.onBackPressed();
        }
    }


    // function used to go to the Habit description page
    private void goToHabitDescriptionActivity(int position, Habit tapHabit){
        Intent intent = new Intent(this, HabitDescriptionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("habit",tapHabit);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}