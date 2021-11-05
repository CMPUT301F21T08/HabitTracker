package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.habittracker.HabitEditActivity;

public class HabitListAddListener implements View.OnClickListener{
    // attributes
    Context context;
    Activity activity;

    public HabitListAddListener(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        // go to habit edit page
        Intent intent = new Intent(context, HabitEditActivity.class);
        intent.putExtra("action", "add");
        activity.startActivity(intent);
        activity.finish(); // finish activity
    }
}
