package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.habittracker.FollowingActivity;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEventListActivity;
import com.example.habittracker.MainPageActivity;
import com.example.habittracker.ProfileActivity;
import com.example.habittracker.R;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class HabitListNavigationBarClickListener implements NavigationBarView.OnItemSelectedListener {
    Context context;
    Activity activity;
    String uid;
    ArrayList<Habit> habitList;

    public HabitListNavigationBarClickListener(Context context, Activity activity, String uid, ArrayList<Habit> habitList){
        this.context = context;
        this.activity = activity;
        this.uid = uid;
        this.habitList = habitList;

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        for(int i = 0; i < habitList.size(); i++){
            Habit habit = habitList.get(i);
            // upload the information to database to update all habit
            HashMap<String, Object> map = new HashMap<>();
            map.put(habit.getUUID(),habit);
            FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
        }
        switch (item.getItemId()) {
            case R.id.navigation_habit:
                return true;
            case R.id.navigation_habitEvent:
                Intent intent1 = new Intent(context, HabitEventListActivity.class);
                intent1.putExtra("StartMode", "normal");
                activity.startActivity(intent1);
                activity.finish();
                return true;
            case R.id.navigation_homePage:
                Intent intent2 = new Intent(context, MainPageActivity.class);
                activity.startActivity(intent2);
                activity.finish();
                return true;
            case R.id.navigation_following:
                Intent intent3 = new Intent(context, FollowingActivity.class);
                activity.startActivity(intent3);
                activity.finish();
                return true;
            case R.id.navigation_settings:
                Intent intent4 = new Intent(context, ProfileActivity.class);
                activity.startActivity(intent4);
                activity.finish();
                return true;
        }
        return false;
    }

}
