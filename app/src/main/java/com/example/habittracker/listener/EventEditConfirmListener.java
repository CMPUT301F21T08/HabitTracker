package com.example.habittracker.listener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.FileUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.habittracker.HabitEvent;
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
    Context context;
    Activity activity;

    // Attribute values
    int eventIndexInList;
    HabitEvent passedEvent;
    boolean addedPhoto;
    String imageFilePath;
    String uid;
    Uri storageURL;

    public EventEditConfirmListener(Context context, Activity activity, ProgressDialog editEventProgressDialog, EditText comment_editText,
                                    EditText location_editText, int eventIndexInList, HabitEvent passedEvent, boolean addedPhoto, String imageFilePath, String uid) {
        this.context = context;
        this.activity= activity;
        this.editEventProgressDialog = editEventProgressDialog;
        this.comment_editText = comment_editText;
        this.location_editText = location_editText;
        this.eventIndexInList = eventIndexInList;
        this.passedEvent = passedEvent;
        this.addedPhoto = addedPhoto;
        this.imageFilePath = imageFilePath;
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
            if (addedPhoto) {
                uploadImage(imageFilePath); // The following functions of the confirm button will be realized in this function to ensure process synchronization
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
                uploadImage(imageFilePath);
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

    /**
     * This function is used to upload an image to the firebase storage
     * This can only be called in the onCLickListener of confirm button
     * If the upload is successful, we get the url of the uploaded image and upload new event imformation to real time storage
     * This takes care of concurrency problem
     * @param imagePath
     */
    public void uploadImage(String imagePath) {

//        verifyStoragePermissions(this); // First always verify permission
        String habitEventUUID = passedEvent.getUuid();
        String imageName = habitEventUUID+".jpg"; // Generate image name: habit_event_name.jpg

        Uri uri = Uri.fromFile(new File(imagePath));

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/users/"+ uid + "/" + imageName); // get storage reference
        storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        storageURL = uri;
                        System.out.println("-----------------> Get photo url success! URL: " + storageURL.toString());

                        passedEvent.setDownloadUrl(storageURL.toString()); // Add photo url to the habit event for further use in retrieval

                        // Upload information to real time database
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(passedEvent.getUuid(),passedEvent);
                        FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent").updateChildren(map);

                        // shift back to list activity
                        Intent intentReturn = new Intent(context, HabitEventListActivity.class); // Return to the habit event list page
                        intentReturn.putExtra("StartMode", "Edit");
                        intentReturn.putExtra("EventIndex", eventIndexInList);
                        intentReturn.putExtra("HabitEventFromEdit", passedEvent);

                        context.startActivity(intentReturn);
                        editEventProgressDialog.dismiss();
                        activity.finish(); // finish current activity
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("-----------------> Get photo url failed!");
                        editEventProgressDialog.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("-----------------> upload photo failed!");
                editEventProgressDialog.dismiss();
            }
        });
    }
}
