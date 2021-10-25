package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;


public class HabitEventEditMapActivity extends AppCompatActivity {
    Button choose_location_button;
    TextView location_information;
    int PLACE_PICKER_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_edit);

        getSupportActionBar().setTitle("Habit Event - Edit");

        Intent intent = getIntent();

        choose_location_button = findViewById(R.id.habitEvent_chooseLocation_button);
        location_information = findViewById(R.id.habitEvent_locationInfo_textView);
        final String TAG = HabitEventEditMapActivity.class.getSimpleName();

        choose_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(HabitEventEditMapActivity.this)
                            ,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, "onCLick: GooglePlayServicesRepairableException: " +e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "onCLick: GooglePlayServicesNotAvailableException: " +e.getMessage());
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                StringBuilder stringBuilder = new StringBuilder();

                String address = String.valueOf(place.getAddress());
                String name = String.valueOf(place.getName());
                stringBuilder.append("Name: ");
                stringBuilder.append(name);
                stringBuilder.append("Address: ");
                stringBuilder.append(address);
                stringBuilder.append("/n");

                location_information.setText(stringBuilder.toString());
            }
        }
    }

}