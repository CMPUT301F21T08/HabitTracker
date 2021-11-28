package com.example.habittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * a fragment allows user to see weekly occurrence day of habit
 */
public class ShowWeekDaysFragment extends DialogFragment {
    // the arraylist used to store the weekly occurrence days
    private ArrayList<Integer> days;
    // the view in the fragment
    private CheckBox Monday;
    private CheckBox Tuesday;
    private CheckBox Wednesday;
    private CheckBox Thursday;
    private CheckBox Friday;
    private CheckBox Saturday;
    private CheckBox Sunday;
    // the variable used to store the habit object
    private Habit habit;

    /**
     * Constructor that takes in the customized warning text
     * @param habit the habit of the weekly occurrence days that the user want to check
     */
    public ShowWeekDaysFragment(Habit habit) {
        super();
        this.habit = habit;
    }



    /**
     * The "onAttach" method for DialogFragment
     * @param context context of the activity
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }



    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // set up the views of the fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_weekly_occurrence_day, null);
        Monday = view.findViewById(R.id.checkbox_monday);
        Tuesday = view.findViewById(R.id.checkbox_tuesday);
        Wednesday = view.findViewById(R.id.checkbox_wednesday);
        Thursday = view.findViewById(R.id.checkbox_thursday);
        Friday = view.findViewById(R.id.checkbox_friday);
        Saturday = view.findViewById(R.id.checkbox_saturday);
        Sunday = view.findViewById(R.id.checkbox_sunday);
        // set all the corresponding checkbox of week day in the occurrence day list to be checked
        setCheckBox(habit);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Weekly occurrence days of habit")
                .setNegativeButton("Return", null).create();

    }

    /**
     * The method used to set up the checkbox in the fragment to show the weekly occurrence days of the habit
     * @param habit the current habit
     */
    private void setCheckBox(Habit habit){
        ArrayList<Integer> days = habit.getOccurrenceDay();
        if(days.contains(2)){
            Monday.setChecked(true);
        }
        if(days.contains(3)){
            Tuesday.setChecked(true);
        }
        if(days.contains(4)){
            Wednesday.setChecked(true);
        }
        if(days.contains(5)){
            Thursday.setChecked(true);
        }
        if(days.contains(6)){
            Friday.setChecked(true);
        }
        if(days.contains(7)){
            Saturday.setChecked(true);
        }
        if(days.contains(1)){
            Sunday.setChecked(true);
        }
    }

}
