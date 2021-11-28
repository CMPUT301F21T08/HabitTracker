package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.habittracker.listener.EventListClickListener;
import com.example.habittracker.listener.NavigationBarClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HabitEventListActivity extends AppCompatActivity {
    // UI view objects
    ListView habitEventListView;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventList;

    BottomNavigationView bottomNavigationView;

    private FirebaseAuth authentication;
    private String uid; // unique id for each user


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_list);

//----------------------------UI Setup----------------------------------------------------------------------
        getSupportActionBar().setTitle("Habit Events");

        habitEventListView = findViewById(R.id.lv_habit_event);

        habitEventList = new ArrayList<HabitEvent>();



        habitEventAdapter = new HabitEventListAdapter(this, habitEventList);
        habitEventListView.setAdapter(habitEventAdapter); // Sets the adapter for event list, used for showing list items


//-------------------------------------------------- FireBase-------------------------------------------------------------------------------------------------------------

        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent");


//----------------------------------update listView -----------------------------------------------

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habitEventList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HabitEvent habitE = (HabitEvent) dataSnapshot.getValue(HabitEvent.class);
                    habitEventList.add(habitE);

                }
                habitEventAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read Data Failed");
            }
        });


//--------------------------------------------- Process List View-----------------------------------------------------------------------------------------------------

        AdapterView.OnItemClickListener habitEventListListener = new EventListClickListener(getApplicationContext(),this,habitEventAdapter);
        habitEventListView.setOnItemClickListener(habitEventListListener);


//---------------------------------------- Process Navigation Bar-----------------------------------------------------------------------------------------------------
        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_habitEvent);

        NavigationBarView.OnItemSelectedListener bottomNavigationViewOnItemSelectedListener = new NavigationBarClickListener(getApplicationContext(),this);
        bottomNavigationView.setOnItemSelectedListener(bottomNavigationViewOnItemSelectedListener);

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
    }

    /**
     * This method is used to ensure the getIntent() method always returns the latest intent
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * This method is used to control the workflow when user press the back button (one device, not in app)
     * Will return to main page directly
     */
    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(this, MainPageActivity.class);
        startActivity(intent3);
        finish();
    }
}