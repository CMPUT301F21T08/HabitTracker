package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HabitEventEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_edit);

        getSupportActionBar().setTitle("Habit Event - Edit");

        Intent intent = getIntent();

        Button cancelBtn = findViewById(R.id.habitEvent_delete_button);

        // set return function to the cancel button
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReturn = new Intent(getApplicationContext(), HabitEventListActivity.class); // Return to the habit event list page
                startActivity(intentReturn);
                finish(); // finish current activity
            }
        });
    }


}