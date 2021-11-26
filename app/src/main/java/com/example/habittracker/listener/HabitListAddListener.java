package com.example.habittracker.listener;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.habittracker.HabitEditActivity;
import com.google.android.material.tabs.TabLayout;

public class HabitListAddListener implements View.OnClickListener{
    // attributes
    Context context;
    Activity activity;
    int amount;

    public HabitListAddListener(Context context, Activity activity, int amount){
        this.context = context;
        this.activity = activity;
        this.amount = amount;
    }

    @Override
    public void onClick(View view) {
        // go to habit edit page
        Intent intent = new Intent(context, HabitEditActivity.class);
        intent.putExtra("action", "add");
        intent.putExtra("amount", amount);
        Log.d(TAG, "fromPosition is " + amount);
        activity.startActivity(intent);
        activity.finish(); // finish activity
    }
}
