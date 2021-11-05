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

public class HabitEventsOfHabitActivity extends AppCompatActivity {
    private ArrayList<String> uuid;
    private ArrayList<String> habitEventTitle;
    private ArrayList<HabitEvent> habitEventsList = new ArrayList<>();
    private ListView habitEventListView;
    private ArrayAdapter<String> habitEventAdapter;
    private FirebaseAuth authentication;
    private String uid;
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_habit_event_list);

        getSupportActionBar().setTitle("Habit Event - Inside Habit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Add a return button to the toolbar

        habitEventListView = findViewById(R.id.habit_habitEvent_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        habit = (Habit) bundle.getSerializable("habit");
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        uuid = habit.getEventList();
        habitEventTitle = new ArrayList<>();
        habitEventAdapter = new ArrayAdapter<String>(this, R.layout.content_habit_habit_event, habitEventTitle);
        habitEventListView.setAdapter(habitEventAdapter);

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
//                intentReturn.putExtra("StartMode", "normal");
                startActivity(intentReturn);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
