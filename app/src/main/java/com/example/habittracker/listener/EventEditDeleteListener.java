package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.habittracker.HabitEvent;
import com.example.habittracker.HabitEventEditActivity;
import com.example.habittracker.HabitEventListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventEditDeleteListener implements DialogInterface.OnClickListener {

    // Attribute variables
    String habitEventUUID;
    String uid;
    HabitEvent passedEvent;
    int eventIndexInList;

    Context context;
    Activity activity;

    public EventEditDeleteListener(String uid, HabitEvent passedEvent, int eventIndexInList, Context context, Activity activity) {
        this.activity = activity;
        this.context = context;
        this.passedEvent = passedEvent;
        this.habitEventUUID = this.passedEvent.getUuid();
        this.uid = uid;
        this.eventIndexInList = eventIndexInList;
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Intent intentReturn = new Intent(context, HabitEventListActivity.class); // Return to the habit event list page

        intentReturn.putExtra("StartMode", "Delete");
        intentReturn.putExtra("EventIndex", eventIndexInList);

        FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent").child(habitEventUUID).removeValue();  // Delete the record in realtime database

        // If the current event has photo uploaded, we delete that photo as well
        if (passedEvent.getDownloadUrl() != null) {
            // delete firebase storage image
            StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(passedEvent.getDownloadUrl());
            reference.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            System.out.println("------------------> Image successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity,"Error in removing image",Toast.LENGTH_SHORT).show();

                        }
                    });
        }

        HabitEventEditActivity.deleteEventFromHabit(passedEvent.getHabitName(), passedEvent.getUuid(), uid, passedEvent.getHabitUUID());  // When deleting an event, we also need to remove its record from the stored list in corresponding habit
        activity.startActivity(intentReturn);
        activity.finish(); // finish current activity
    }
}
