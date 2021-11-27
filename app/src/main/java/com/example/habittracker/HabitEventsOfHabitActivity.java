package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Allow users to see the list of habit event of the habit
 *
 */
public class HabitEventsOfHabitActivity extends AppCompatActivity {
    // the id list of habit event related to habit
    private ArrayList<String> uuid;
    private ArrayList<String> habitEventTitle;
    private ArrayList<HabitEvent> habitEventsList = new ArrayList<>();
    private ListView habitEventListView;
    private ArrayAdapter<String> habitEventAdapter;
    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_habit_event_list);

        getSupportActionBar().setTitle("Habit Event - Inside Habit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Add a return button to the toolbar

        habitEventListView = findViewById(R.id.habit_habitEvent_list);

        // get the habit
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        habit = (Habit) bundle.getSerializable("habit");

        // database connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // get the id list of habit event related to habit
        uuid = habit.getEventList();
        habitEventTitle = new ArrayList<>();
        habitEventAdapter = new ArrayAdapter<String>(this, R.layout.content_habit_habit_event, habitEventTitle);
        habitEventListView.setAdapter(habitEventAdapter);

        // get all the habit events from the database and put their title in the habitEventTitle arrayList
        for (int i=0; i<uuid.size();i++){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent").child(uuid.get(i));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HabitEvent habE = (HabitEvent) snapshot.getValue(HabitEvent.class);
                    if (habE != null) {
                        habitEventsList.add(habE);
                        habitEventTitle.add(habE.getEventTitle());
                        habitEventAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("Read Data Failed");
                }
            });
        }

        // set up the OnItemClickListener to go to the habit event edit page to allow user edit the habit event
        habitEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HabitEvent habitEvent = habitEventsList.get(i);
                Intent intent = new Intent(HabitEventsOfHabitActivity.this, HabitEventEditActivity.class);
                intent.putExtra("HabitEventForEdit", habitEvent);
                intent.putExtra("EventIndex", i);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Customize the function of the return button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intentReturn = new Intent(getApplicationContext(), HabitListActivity.class); // Return to the habit event list page
                startActivity(intentReturn);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Process the KEY_RETURN signal in the habit edit activity
     * When back button is pressed, return to habit list activity
     */
    @Override
    public void onBackPressed() {
        Intent intentReturn = new Intent(getApplicationContext(), HabitListActivity.class); // Return to the habit event list page
        startActivity(intentReturn);
        finish();
    }
}
