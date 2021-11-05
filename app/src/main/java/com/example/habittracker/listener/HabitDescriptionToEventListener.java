package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEventsOfHabitActivity;

public class HabitDescriptionToEventListener implements View.OnClickListener{
    // attributes
    private Habit habit;
    private Context context;
    private Activity activity;

    public HabitDescriptionToEventListener(Context context, Activity activity, Habit habit){
        // attributes
        this.context = context;
        this.activity = activity;
        this.habit = habit;

    }

    @Override
    public void onClick(View view) {
        // go to the HabitEventsOfHabitActivity to see all the habit events related to current event
        Intent intentEvent = new Intent(context, HabitEventsOfHabitActivity.class);
        Bundle bundleEvent = new Bundle();
        bundleEvent.putSerializable("habit", habit);
        intentEvent.putExtras(bundleEvent);
        activity.startActivity(intentEvent);
        activity.finish(); // finish the current activity
    }
}
