package com.example.habittracker.listener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.habittracker.FollowingActivity;

import com.example.habittracker.HabitEventListActivity;
import com.example.habittracker.HabitListActivity;
import com.example.habittracker.MainPageActivity;
import com.example.habittracker.ProfileActivity;
import com.example.habittracker.R;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationBarClickListener implements NavigationBarView.OnItemSelectedListener{
    Context context;
    Activity activity;

    public NavigationBarClickListener(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        System.out.println("cannot run case");
        switch (item.getItemId()) {
            case R.id.navigation_habit:
                Intent intent1 = new Intent(context, HabitListActivity.class);
                activity.startActivity(intent1);
                activity.finish();
                return true;
            case R.id.navigation_habitEvent:
                Intent intent2 = new Intent(context, HabitEventListActivity.class);
                intent2.putExtra("StartMode", "normal");
                activity.startActivity(intent2);
                activity.finish();
                return true;
            case R.id.navigation_homePage:
                Intent intent3 = new Intent(context, MainPageActivity.class);
                activity.startActivity(intent3);
                activity.finish();
                return true;
            case R.id.navigation_following:
                Intent intent4 = new Intent(context, FollowingActivity.class);
                activity.startActivity(intent4);
                activity.finish();
                return true;
            case R.id.navigation_settings:
                Intent intent5 = new Intent(context, ProfileActivity.class);
                activity.startActivity(intent5);
                activity.finish();
                return true;
        }
        System.out.println("cannot getItemId");
        return false;
    }
}
