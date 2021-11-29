package com.example.habittracker.listener;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEditActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class HabitListAddListener implements View.OnClickListener{
    // attributes
    Context context;
    Activity activity;
    int amount;
    ArrayList<Habit> habitList;
    String uid;

    public HabitListAddListener(Context context, Activity activity, int amount,ArrayList<Habit> habitList, String ui ){
        this.context = context;
        this.activity = activity;
        this.amount = amount;
        this.habitList = habitList;
        this.uid = uid;

    }

    @Override
    public void onClick(View view) {
        for(int i = 0; i < habitList.size(); i++){
            Habit habit = habitList.get(i);
            // upload the information to database to update all habit
            HashMap<String, Object> map = new HashMap<>();
            map.put(habit.getUUID(),habit);
            FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
        }
        Intent intent = new Intent(context, HabitEditActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("action", "add");
        activity.startActivity(intent);
        activity.finish();
    }
}
