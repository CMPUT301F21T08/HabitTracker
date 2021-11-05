package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEditActivity;

public class HabitDescriptionEditListener implements View.OnClickListener{
    // attributes
    private String action;
    private Context context;
    private ActivityResultLauncher<Intent> activityLauncher;
    private Habit habit;

    public HabitDescriptionEditListener(Context context,ActivityResultLauncher<Intent> activityLauncher, Habit habit){
        this.context = context;
        this.activityLauncher = activityLauncher;
        this.habit = habit;
    }

    @Override
    public void onClick(View view) {
        // send the habit object and the mode signal
        action = "new";
        Intent intentEdit = new Intent(context, HabitEditActivity.class);
        intentEdit.putExtra("action", "edit");
        Bundle bundle= new Bundle();
        bundle.putSerializable("habit", habit);
        intentEdit.putExtras(bundle);
        // ask for a result return from HabitEditActivity
        activityLauncher.launch(intentEdit);
    }
}
