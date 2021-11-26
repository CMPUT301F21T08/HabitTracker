package com.example.habittracker.listener;

import static com.example.habittracker.HabitEditActivity.value_of_OccurrenceDate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEditActivity;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.HabitListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HabitEditConfirmListener implements View.OnClickListener{
    // attributes related to view
    private Context context;
    private Activity activity;
    private EditText title;
    private EditText content;
    private EditText reason;
    private EditText date;
    private EditText frequency;
    private TextView frequencyType;

    // habit object
    private Habit habit;
    // variable used to set up a AlertDialog
    private AlertDialog.Builder builder;
    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID
    // result code
    private int newObject;
    private int index;


    public HabitEditConfirmListener(Context context, Activity activity, EditText title, EditText content, EditText reason, EditText date, EditText frequency, TextView frequencyType, Habit habit, FirebaseAuth authentication, String uid, int newObject, int index){
        this.context = context;
        this.activity = activity;
        this.title = title;
        this.content = content;
        this.reason = reason;
        this.date = date;
        this.frequency = frequency;
        this.frequencyType = frequencyType;
        this.habit = habit;
        this.authentication = authentication;
        this.uid = uid;
        this.newObject = newObject;
        this.index = index;
    }

    @Override
    public void onClick(View view) {
        builder = new AlertDialog.Builder(activity);
        // call this function to check whether all data has been input and all data is legal
        if(isValidInput()){
            // if the habit is not null, that means this page is used to edit a habit
            if(habit != null){
                // if the user change the title of the habit, we need to remove the habit from the database first
                // and then upload the habit with the new title

                // If the user has modified the title for a habit, we need to change the title for all its corresponding habit events
                if(!habit.getHabitTitle().equals(title.getText().toString())){
                    for (int i = 0; i < habit.getEventList().size(); i++) {
                        changeTitleOfAllEvents(title.getText().toString(), habit.getEventList().get(i));
                    }
                }

                // use setter method of the attributes to renew the habit
                habit.setHabitTitle(title.getText().toString());
                habit.setFrequency(Integer.parseInt(frequency.getText().toString()));
                habit.setFrequencyType(frequencyType.getText().toString());
                habit.setStartDate(date.getText().toString());
                habit.setHabitContent(content.getText().toString());
                habit.setHabitReason(reason.getText().toString());
                habit.setOccurrenceDay(value_of_OccurrenceDate);

                // upload the habit to the database
                HashMap<String, Object> map = new HashMap<>();
                map.put(habit.getUUID(), habit);
                FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                // send back the habit to HabitDescription page
                Intent intentDes = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", habit);
                intentDes.putExtras(bundle);
                activity.setResult(newObject, intentDes);
            } else {
                // create a new habit with information user input and go back to HabitList activity
                Intent intentConfirm = new Intent(context, HabitListActivity.class);
                String value_of_title = title.getText().toString();
                String value_of_frequencyType = frequencyType.getText().toString();
                int value_of_frequency = Integer.parseInt(frequency.getText().toString());
                String value_of_startDate = date.getText().toString();
                String value_of_content = content.getText().toString();
                String value_of_reason = reason.getText().toString();
                // vlaue_of_occurrence is a global variable
                String uuid = UUID.randomUUID().toString(); // Generate the unique uuid for each habit
                habit = new Habit(value_of_title, value_of_reason, value_of_content, value_of_startDate, value_of_frequency, value_of_frequencyType, value_of_OccurrenceDate, uuid, index);
                // adding habit into the firebase
                HashMap<String, Object> map = new HashMap<>();
                map.put(habit.getUUID(),habit);
                FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                activity.startActivity(intentConfirm);
            }
            activity.finish();
        } else {
            // if the user does not input all required information, call a dialog to notice them
            AlertDialog alert;
            alert = builder
                    .setTitle("Warning:")
                    .setMessage("Please enter all required information.")
                    .setNegativeButton("return", null).create();
            alert.show();
        }
    }

    /**
     * This method changed all the titles of the events in the provided event list
     * @param newHabitTitle
     * @param eventUUID
     */
    private void changeTitleOfAllEvents(String newHabitTitle, String eventUUID) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HabitEvent habitEvent = (HabitEvent) dataSnapshot.getValue(HabitEvent.class);

                    if (habitEvent.getUuid().equals(eventUUID)) {
                        habitEvent.setHabitName(newHabitTitle);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put(habitEvent.getUuid(),habitEvent);
                        FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent").updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // function used to check whether all required information is received
    private boolean isValidInput(){
        boolean validInput = true;
        if(title.getText().toString().trim().length()==0){
            validInput = false;
        }
        if(reason.getText().toString().trim().length()==0){
            validInput = false;
        }
        if(content.getText().toString().trim().length()==0){
            validInput = false;
        }
        if(frequency.getText().toString().trim().length()==0){
            validInput = false;
        }
        if(date.getText().toString().trim().length()==0){
            validInput = false;
        }
        return validInput;
    }
}
