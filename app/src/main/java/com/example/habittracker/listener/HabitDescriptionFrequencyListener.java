package com.example.habittracker.listener;

import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.habittracker.Habit;
import com.example.habittracker.ShowWeekDaysFragment;

public class HabitDescriptionFrequencyListener implements View.OnClickListener {
    FragmentManager fragmentManager;
    Habit habit;

    public HabitDescriptionFrequencyListener(FragmentManager fragmentManager, Habit habit){
        this.fragmentManager = fragmentManager;
        this.habit = habit;

    }

    @Override
    public void onClick(View view) {

        // if the frequencyType is "per week", then invoke a fragment to show the occurrence week day of the habit
        if(habit.getFrequencyType().equals("per week")){
            new ShowWeekDaysFragment(habit).show(fragmentManager,"SHOW_OCCURRENCE_DAYS");
        }
    }

}
