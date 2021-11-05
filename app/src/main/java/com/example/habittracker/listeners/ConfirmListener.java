package com.example.habittracker.listeners;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.example.habittracker.HabitEvent;
import com.example.habittracker.HabitEventListActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ConfirmListener implements View.OnClickListener{
    ProgressDialog editEventProgressDialog;
    EditText location_editText;
    EditText comment_editText;

    HabitEvent passedEvent;
    int eventIndexInList;
    boolean addedPhoto;

    private String uid; // User unique ID
    String imageFilePath;

    Context context;
    Activity activity;

    public ConfirmListener(ProgressDialog progressDialog, EditText location_editText, EditText comment_editText, HabitEvent passedEvent, int eventIndexInList, boolean addedPhoto, String uid, String imageFilePath, Context context, Activity activity) {
        this.editEventProgressDialog = progressDialog;
        this.location_editText = location_editText;
        this.comment_editText = comment_editText;
        this.passedEvent = passedEvent;
        this.eventIndexInList = eventIndexInList;
        this.addedPhoto = addedPhoto;
        this.uid = uid;
        this.imageFilePath = imageFilePath;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        // First create a progress dialogue showing that we are currently processing this habit event
        editEventProgressDialog.setMessage("Processing");
        editEventProgressDialog.show();

        // Create intent that is used to return to event list view
        Intent intentReturn = new Intent(context, HabitEventListActivity.class); // Return to the habit event list page

        intentReturn.putExtra("StartMode", "Edit");

        String comment = comment_editText.getText().toString();
        String location = location_editText.getText().toString();

        if (eventIndexInList >= 0) {
            // modify current entry, put necessary information and pass them to list activity
            passedEvent.setComment(comment);
            passedEvent.setLocation(location);

            // If the user selected photo from phone, we upload this photo and finish the rest of the process there
            if (addedPhoto) {
//                uploadImage(imageFilePath); // The following functions of the confirm button will be realized in this function to ensure process synchronization
                return;
            }

            // Upload information to real time database
            HashMap<String, Object> map = new HashMap<>();
            map.put(passedEvent.getUuid(), passedEvent);  // habit events are stored in the database under their uuid
            FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent").updateChildren(map);

            // These to are required when we return to the main activity
            intentReturn.putExtra("EventIndex", eventIndexInList);
            intentReturn.putExtra("HabitEventFromEdit", passedEvent);
        }
        else {
            // create new entry
            passedEvent.setComment(comment);
            passedEvent.setLocation(location);

            // If the user selected photo from phone, we upload this photo and finish the rest of the process there
            if (addedPhoto) {
//                uploadImage(imageFilePath);
                return;
            }

            intentReturn.putExtra("EventIndex", eventIndexInList);
            intentReturn.putExtra("HabitEventFromEdit", passedEvent);

            HashMap<String, Object> map = new HashMap<>();
            map.put(passedEvent.getUuid(), passedEvent);

            FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent").updateChildren(map);  // update firebase
        }

        editEventProgressDialog.dismiss();

        context.startActivity(intentReturn);
        activity.finish(); // finish current activity
    }
}
