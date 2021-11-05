/**
 * @author 'yhu19' and 'ingabire'
 * Allow users to see the list of habits they have
 *
 */

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
    // views of the activity
    private ListView habitListView;
    private FloatingActionButton addButton;
    private ArrayAdapter<Habit> habitAdapter;
    private ArrayList<Habit> habitList;

    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID



    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);
        getSupportActionBar().setTitle("Habit - All Habits");
        // set up the view for the activity
        habitListView = findViewById(R.id.allHabits_habitList_listView);
        addButton = findViewById(R.id.allHabits_addButton_button);

        // firebase connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // create the listView using the habit arrayList
        habitList = new ArrayList<Habit>();
        habitAdapter = new HabitListAdapter(this, habitList);
        habitListView.setAdapter(habitAdapter);

        // get all the habit the user has from the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habitList.clear();
                // using the habits that retrieve from the database to set upt listView
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



        // set up the OnItemClickListener to allow the user to click on the habit to see its detailed information
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit tapHabit = habitList.get(i);
                goToHabitDescriptionActivity(i, tapHabit);

            }
        });

        // set up the OnClickListener to allow user to add a new habit
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HabitEditActivity.class);
                intent.putExtra("action", "add");
                startActivity(intent);
                finish();
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
                        Intent intent2 = new Intent(HabitListActivity.this, MainPageActivity.class);
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

    // a function used to go to the habit description page with the tapped habit
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
