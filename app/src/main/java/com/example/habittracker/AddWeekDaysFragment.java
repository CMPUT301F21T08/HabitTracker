package com.example.habittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class AddWeekDaysFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private ArrayList<Integer> days =  new ArrayList<Integer>();
    private int valueOfFrequency = 0;
    private CheckBox Monday;
    private CheckBox Tuesday;
    private CheckBox Wednesday;
    private CheckBox Thursday;
    private CheckBox Friday;
    private CheckBox Saturday;
    private CheckBox Sunday;

    public AddWeekDaysFragment(){
        super();
    }

    public interface OnFragmentInteractionListener{
        void onConfirmPressed(ArrayList<Integer> days, int valueOfFrequency);
        //void onReturnPressed();
    }

    /**
     * The "onAttach" method for DialogFragment, also creates a listener interface
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+"need to implement OnFragmentInteractionListener");
        }
    }


    @NonNull
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
        setAllClickable();

        // create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Choose Weekly Occurrence date")
                // create "Ok" button
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    // extract the information from EditText to set up a new Medicine Object
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(Monday.isChecked()){
                            days.add(1);
                            valueOfFrequency++;
                        }
                        if(Tuesday.isChecked()){
                            days.add(2);
                            valueOfFrequency++;
                        }
                        if(Wednesday.isChecked()){
                            days.add(3);
                            valueOfFrequency++;
                        }
                        if(Thursday.isChecked()){
                            days.add(4);
                            valueOfFrequency++;
                        }
                        if(Friday.isChecked()){
                            days.add(5);
                            valueOfFrequency++;
                        }
                        if(Saturday.isChecked()){
                            days.add(6);
                            valueOfFrequency++;
                        }
                        if(Sunday.isChecked()){
                            days.add(7);
                            valueOfFrequency++;
                        }
                        listener.onConfirmPressed(days, valueOfFrequency);
                    }
                }).create();

    }

    private void setAllClickable(){
        Monday.setClickable(true);
        Tuesday.setClickable(true);
        Wednesday.setClickable(true);
        Thursday.setClickable(true);
        Friday.setClickable(true);
        Saturday.setClickable(true);
        Sunday.setClickable(true);
    }
}