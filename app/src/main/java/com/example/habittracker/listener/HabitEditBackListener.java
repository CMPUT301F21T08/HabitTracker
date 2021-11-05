package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.habittracker.HabitEditActivity;
import com.example.habittracker.HabitListActivity;

public class HabitEditBackListener implements View.OnClickListener{
    // attributes
    Context context;
    Activity activity;
    String action;
    int original;
    String message;

    public HabitEditBackListener(Context context, Activity activity, String action, int original, String message){
        this.context = context;
        this.activity = activity;
        this.action = action;
        this.original = original;
        this.message = message;
    }


    @Override
    public void onClick(View view) {
        // this for statement will determine what is the last activity by comparing the action string from the last activity with "add"
        Intent intentReturn;
        if(message.equals("add")){
            // go back to HabitListActivity
            intentReturn = new Intent(context, HabitListActivity.class);
            activity.startActivity(intentReturn);
        } else {
            // go back to HabitDescriptionActivity with the modified habit
            intentReturn = new Intent();
            action = "original";
            intentReturn.putExtra("action", action);
            activity.setResult(original, intentReturn);
        }
        activity.finish();
    }
}
