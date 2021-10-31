package com.example.habittracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HabitListActivity extends AppCompatActivity {
    // Result code
    private int back = 11;
    private int delete = 22;
    private int add = 55;
    private ListView habitListView;
    private FloatingActionButton addButton;
    private ArrayAdapter<Habit> habitAdapter;
    private ArrayList<Habit> habitList;

    private FirebaseAuth authentication;
    private String uid;

    private ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == back) {
                        Intent data = result.getData();
                        String action = data.getStringExtra("action");
                        if(action.equals("new")){
                            habitAdapter.notifyDataSetChanged();
                        }

                    }
                    if(result.getResultCode() == delete){
                        Intent data = result.getData();
                        int position = Integer.parseInt(data.getStringExtra("position"));
                        habitList.remove(position);
                        habitAdapter.notifyDataSetChanged();
                    }
                    if(result.getResultCode() == add) {
                        Intent data = result.getData();
                        Habit newHabit = (Habit) data.getExtras().getSerializable("habit");
                        habitList.add(newHabit);
                        habitAdapter.notifyDataSetChanged();
                    }
                }
            });


    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);
        getSupportActionBar().setTitle("Habit - All Habits");
        habitListView = findViewById(R.id.allHabits_habitList_listView);
        addButton = findViewById(R.id.allHabits_addButton_button);

        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        habitList = new ArrayList<Habit>();
        habitAdapter = new HabitListAdapter(this, habitList);
        habitListView.setAdapter(habitAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Habit hab = (Habit) dataSnapshot.getValue(Habit.class);
                    habitList.add(hab);

                }
                habitAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read Data Failed");
            }
        });




        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit tapHabit = habitList.get(i);
                goToHabitDescriptionActivity(i, tapHabit);

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HabitEditActivity.class);
                intent.putExtra("action", "add");
                activityLauncher.launch(intent);
            }
        });




        // Process Navigation Bar
        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_habit);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_habit:
                        return true;
                    case R.id.navigation_habitEvent:
                        Intent intent1 = new Intent(HabitListActivity.this, HabitEventListActivity.class);
                        intent1.putExtra("StartMode", "normal");
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.navigation_homePage:
                        Intent intent2 = new Intent(HabitListActivity.this, ToDoActivity.class);
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.navigation_following:
                        Intent intent3 = new Intent(HabitListActivity.this, FollowingActivity.class);
                        startActivity(intent3);
                        finish();
                        return true;
                    case R.id.navigation_settings:
                        Intent intent4 = new Intent(HabitListActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    private void goToHabitDescriptionActivity(int position, Habit tapHabit){
        Intent intent = new Intent(this, HabitDescriptionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("habit",tapHabit);
        intent.putExtras(bundle);
        activityLauncher.launch(intent);
    }
}