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


public class ShowWeekDaysFragment extends DialogFragment {
    //private ShowWeekDaysFragment.OnFragmentInteractionListener listener;
    private ArrayList<Integer> days;
    private CheckBox Monday;
    private CheckBox Tuesday;
    private CheckBox Wednesday;
    private CheckBox Thursday;
    private CheckBox Friday;
    private CheckBox Saturday;
    private CheckBox Sunday;
    private Habit habit;



    // TODO: Rename and change types of parameters


    public ShowWeekDaysFragment(Habit habit) {
        super();
        this.habit = habit;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }



    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_weekly_occurrence_day, null);

        Monday = view.findViewById(R.id.checkbox_monday);
        Tuesday = view.findViewById(R.id.checkbox_tuesday);
        Wednesday = view.findViewById(R.id.checkbox_wednesday);
        Thursday = view.findViewById(R.id.checkbox_thursday);
        Friday = view.findViewById(R.id.checkbox_friday);
        Saturday = view.findViewById(R.id.checkbox_saturday);
        Sunday = view.findViewById(R.id.checkbox_sunday);
        setCheckBox(habit);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Weekly occurrence days of habit")
                .setNegativeButton("Return", null).create();

    }

    private void setCheckBox(Habit habit){
        ArrayList<Integer> days = habit.getOccurrenceDay();
        if(days.contains(1)){
            Monday.setChecked(true);
        }
        if(days.contains(2)){
            Tuesday.setChecked(true);
        }
        if(days.contains(3)){
            Wednesday.setChecked(true);
        }
        if(days.contains(4)){
            Thursday.setChecked(true);
        }
        if(days.contains(5)){
            Friday.setChecked(true);
        }
        if(days.contains(6)){
            Saturday.setChecked(true);
        }
        if(days.contains(7)){
            Sunday.setChecked(true);
        }
    }

}
