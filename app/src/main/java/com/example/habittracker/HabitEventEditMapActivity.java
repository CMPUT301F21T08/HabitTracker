package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HabitEventEditMapActivity extends AppCompatActivity {
    Button choose_location_button;
    TextView location_information;
    int PLACE_PICKER_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_edit);
        choose_location_button = findViewById(R.id.habitEvent_chooseLocation_button);
        location_information = findViewById(R.id.habitEvent_locationInfo_textView);


    }
}
