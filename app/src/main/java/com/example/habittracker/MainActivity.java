package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_main);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_habit:
                        return true;
                    case R.id.navigation_habitEvent:
                        switch_activity();
                        return true;
                    case R.id.navigation_following:
                        return true;
                    case R.id.navigation_settings:
                        return true;
                }
                return false;
            }
        });


    }

    public void switch_activity() {
        Intent intent = new Intent(this, HabitEventListActivity.class);
        startActivity(intent);
    }


}