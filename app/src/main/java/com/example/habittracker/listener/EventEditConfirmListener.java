package com.example.habittracker.listener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.FileUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.habittracker.HabitEvent;
import com.example.habittracker.HabitEventEditActivity;
import com.example.habittracker.HabitEventListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class EventEditConfirmListener implements View.OnClickListener{

    // Views and UI elements
    ProgressDialog editEventProgressDialog;
    EditText comment_editText;
    EditText location_editText;
    ImageView photo_imageView;
    Context context;
    Activity activity;

    // Attribute values
    int eventIndexInList;
    HabitEvent passedEvent;
    String uid;
    Uri storageURL;

    public EventEditConfirmListener(Context context, Activity activity, ProgressDialog editEventProgressDialog, EditText comment_editText,
                                    EditText location_editText, int eventIndexInList, HabitEvent passedEvent, ImageView photo_imageView, String uid) {
        this.context = context;
        this.activity= activity;
        this.editEventProgressDialog = editEventProgressDialog;
        this.comment_editText = comment_editText;
        this.location_editText = location_editText;
        this.eventIndexInList = eventIndexInList;
        this.passedEvent = passedEvent;
        this.photo_imageView = photo_imageView;
        this.uid = uid;
        if (this.passedEvent.getDownloadUrl() != null) {
            storageURL = Uri.parse(this.passedEvent.getDownloadUrl());
        }

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
            if (photo_imageView.getDrawable() != null) {
                HabitEventEditActivity.uploadImage(passedEvent.getLocalImagePath(), passedEvent.getUuid(), passedEvent, eventIndexInList, editEventProgressDialog, activity, context, uid);
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
            if (photo_imageView.getDrawable() != null) {
                HabitEventEditActivity.uploadImage(passedEvent.getLocalImagePath(), passedEvent.getUuid(), passedEvent, eventIndexInList, editEventProgressDialog, activity, context, uid);
                return;
            }

            intentReturn.putExtra("EventIndex", eventIndexInList);
            intentReturn.putExtra("HabitEventFromEdit", passedEvent);

            HashMap<String, Object> map = new HashMap<>();
            map.put(passedEvent.getUuid(), passedEvent);

            FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent").updateChildren(map);  // update firebase
        }

        editEventProgressDialog.dismiss();

        activity.startActivity(intentReturn);
        activity.finish(); // finish current activity
    }
}
