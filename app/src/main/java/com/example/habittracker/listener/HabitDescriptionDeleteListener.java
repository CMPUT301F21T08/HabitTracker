package com.example.habittracker.listener;

import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.habittracker.DeleteConfirmFragment;

public class HabitDescriptionDeleteListener implements View.OnClickListener {
    FragmentManager fragmentManager;

    public HabitDescriptionDeleteListener (FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;

    }

    @Override
    public void onClick(View view) {
        // invoke a fragment to ask for confirmation for deletion of the habit from user and delete the habit if user click confirm
        new DeleteConfirmFragment("Are you sure you want to delete?").show(fragmentManager,"DELETE_HABIT");
    }
}
