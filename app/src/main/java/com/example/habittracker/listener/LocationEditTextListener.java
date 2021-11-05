package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;

import com.example.habittracker.HabitEventEditActivity;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class LocationEditTextListener implements View.OnClickListener{

    Activity activity;
    ActivityResultLauncher activityResultLauncher;

    public LocationEditTextListener(Activity activity, ActivityResultLauncher activityResultLauncher) {
        this.activity = activity;
        this.activityResultLauncher = activityResultLauncher;
    }

    @Override
    public void onClick(View view) {
        // initialize place field list
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

        try{
            // Create intent
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fieldList).build(activity);
            activityResultLauncher.launch(intent);
        } catch (Exception e) {
            // TODO: Handle the error.
            Log.e("error", e.getMessage());
            e.printStackTrace();
        }
    }
}
