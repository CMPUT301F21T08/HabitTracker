package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ToDoActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_homePage);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_habit:
                        Intent intent1 = new Intent(ToDoActivity.this, HabitListActivity.class);
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.navigation_habitEvent:
                        Intent intent2 = new Intent(ToDoActivity.this, HabitEventListActivity.class);
                        intent2.putExtra("StartMode", "normal");
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.navigation_homePage:
                        return true;
                    case R.id.navigation_following:
                        Intent intent3 = new Intent(ToDoActivity.this, FollowingActivity.class);
                        startActivity(intent3);
                        finish();
                        return true;
                    case R.id.navigation_settings:
                        Intent intent4 = new Intent(ToDoActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}
