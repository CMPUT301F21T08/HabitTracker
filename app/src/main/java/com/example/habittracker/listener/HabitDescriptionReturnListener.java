package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.habittracker.HabitDescriptionActivity;
import com.example.habittracker.HabitListActivity;

public class HabitDescriptionReturnListener implements View.OnClickListener{
    // attribute
    private Context context;
    private Activity activity;

    public HabitDescriptionReturnListener(Context context, Activity activity){
        this.activity = activity;
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        // go back to the HabitListActivity
        Intent intentReturn = new Intent(context, HabitListActivity.class);
        activity.startActivity(intentReturn);
        activity.finish();//return to the HabitListActivity
    }
}
