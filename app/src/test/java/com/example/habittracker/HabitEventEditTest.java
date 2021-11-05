package com.example.habittracker;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.habittracker.listener.CurrentLocationListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HabitEventEditTest {

    HabitEventEditActivity activity;
    Context context;

    EditText locationEditText;
    Button currentLocationButton;

    @BeforeEach
    public void setUp() {
        activity = new HabitEventEditActivity();
        context = activity.getApplicationContext();

        locationEditText = activity.findViewById(R.id.habitEvent_enterLocation_editText);
        currentLocationButton = activity.findViewById(R.id.habitEvent_currentLocation_button);
    }

    @Test
    public void testGetCurrentLocation() {
        View.OnClickListener currentLocationOnclickListener = new CurrentLocationListener(activity, locationEditText);
        currentLocationOnclickListener.onClick(currentLocationButton);
        String result = locationEditText.getText().toString();
        System.out.println(result);
    }
}
