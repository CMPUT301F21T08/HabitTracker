package com.example.habittracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HabitEventEditActivity extends AppCompatActivity implements DeleteConfirmFragment.OnDeleteConfirmFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_edit);

        getSupportActionBar().setTitle("Habit Event - Edit");

        Intent intent = getIntent();

        Button deleteBtn = findViewById(R.id.habitEvent_delete_button);
        Button confirmBtn = findViewById(R.id.habitEvent_confirm_button);
        // set return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteConfirmFragment("Are you sure you want to delete?").show(getSupportFragmentManager(), "DELETE_HABIT_EVENT");
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReturn = new Intent(getApplicationContext(), HabitEventListActivity.class); // Return to the habit event list page
                startActivity(intentReturn);
                finish(); // finish current activity
            }
        });

    }

    public void onConfirmDeletePressed() {
        return;
    }


}