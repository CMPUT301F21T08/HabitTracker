package com.example.habittracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;


import android.net.Uri;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;


import android.widget.ImageView;


import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.habittracker.listener.EventEditConfirmListener;
import com.example.habittracker.listener.EventEditDeleteListener;
import com.example.habittracker.listener.LocationEditTextListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.widget.ListView;


import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;


public class HabitEventEditActivity extends AppCompatActivity  {

    private FirebaseAuth authentication;  // user authentication reference
    private String uid; // User unique ID


    // UI View elements
    EditText location_editText;
    EditText comment_editText;
    Button photo_button;
    ImageView photo_imageView;
    ProgressDialog editEventProgressDialog;
    ActivityResultLauncher<Intent> activityResultLauncher;

    Button currentLocation_button;
    Button deleteBtn;
    Button confirmBtn;
    FusedLocationProviderClient fusedLocationProviderClient;

    // Global attributes used to store habit event a dn habit related information
    HabitEvent passedEvent;
    String habitName;
    String habitEventTitle;
    String imageFilePath; // This always saves the path for the current image shown in photo_imageView
    String habitEventUUID;
    Uri storageURL;
    int eventIndexInList;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    boolean addedPhoto = false;  // flag that is used to determin whether user has added a photo to the edit page


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_edit);
//-------------------------------------------------- FireBase-------------------------------------------------------------------------------------------------------------

        authentication =FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }


//-------------------------------------------------- Initial setup for the activity-------------------------------------------------------------------------------------------------------------
        deleteBtn = findViewById(R.id.habitEvent_delete_button);
        confirmBtn = findViewById(R.id.habitEvent_confirm_button);
        location_editText = findViewById(R.id.habitEvent_enterLocation_editText);
        comment_editText = findViewById(R.id.habitEvent_comment_editText);
        photo_imageView = findViewById(R.id.habitEvent_photo_imageView);
        deleteBtn = findViewById(R.id.habitEvent_delete_button);
        confirmBtn = findViewById(R.id.habitEvent_confirm_button);
        editEventProgressDialog = new ProgressDialog(HabitEventEditActivity.this);

        getSupportActionBar().setTitle("Habit Event - Edit");
        // set return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//-------------------------------------------------Get passed habit event object from other events------------------------------------------------------------------------------------------------
        Intent intentGetData = getIntent();

        verifyStoragePermissions(this);  // Ask for permission to access storage for later use

        // And display information (if any)
        Bundle data = intentGetData.getExtras();
        eventIndexInList = data.getInt("EventIndex");
        System.out.println("----------------> Event index: " + eventIndexInList);

        if (eventIndexInList >= 0) {
            // In this case we are editing an entry in the list
            passedEvent = (HabitEvent) data.getParcelable("HabitEventForEdit");
            comment_editText.setText(passedEvent.getComment());
            location_editText.setText(passedEvent.getLocation());

            habitEventTitle = passedEvent.getEventTitle();
            habitEventUUID = passedEvent.getUuid();
            habitName = passedEvent.getHabitName();

            System.out.println("-----------------> Habit name: "+habitName);
            System.out.println("-----------------> Habit uuid: "+habitEventUUID);
            System.out.println("-----------------> User uid: "+uid);

            String storageUrlString = passedEvent.getDownloadUrl();
            if (storageUrlString != null) {
                storageURL = Uri.parse(storageUrlString);
                System.out.println("-------------------------> Obtained uri is: " + storageURL.toString());
                loadUrlImageToImageView(storageURL);
            }
            else{
                System.out.println("-------------------------> Image file path is null!");
            }

            // Set the onClickListener for confirm button
            View.OnClickListener confirmBtnOnclickListener = new EventEditConfirmListener(getApplicationContext(), this, editEventProgressDialog, comment_editText, location_editText, eventIndexInList, passedEvent, photo_imageView, uid);
            confirmBtn.setOnClickListener(confirmBtnOnclickListener);
        }
        else {
            // Since the user is required to add a event every time he finishes a habit
            // We hide the return button when adding new event to force user to add event, even a event that is completely empty
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            // disable delete button when first make habit event
            deleteBtn.setEnabled(false);
            // In this case we are adding a new event, hence no manipulation is needed
            habitName = data.getString("HabitName"); //TODO: use this on the habit side to transfer data
            habitEventUUID = data.getString("UniqueID");

            String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
            habitEventTitle = habitName +": "+ date;
            passedEvent = new HabitEvent(habitName, "", "", habitEventUUID);

            // Set the onClickListener for confirm button
            View.OnClickListener confirmBtnOnclickListener = new EventEditConfirmListener(getApplicationContext(), this, editEventProgressDialog, comment_editText, location_editText, eventIndexInList, passedEvent, photo_imageView, uid);
            confirmBtn.setOnClickListener(confirmBtnOnclickListener);
        }

//-------------------------------------------------- delete button -------------------------------------------------------------------------------------------------------------
        DialogInterface.OnClickListener deleteOnclickListener = new EventEditDeleteListener(uid, passedEvent, eventIndexInList, getApplicationContext(), this);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitEventEditActivity.this);
                builder.setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Confirm", deleteOnclickListener)
                        .setNegativeButton("Cancel",null);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


//------------------------------------------------------- Upload photo from phone and display in imageView------------------------------------------------------------------------------------------------------------------------------------
        // reference: https://www.youtube.com/watch?v=HxlAktedIhM

        photo_button = findViewById(R.id.habitEvent_addPhoto_button);

        // Initialize result launcher
        ActivityResultLauncher<Intent> resultLauncher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // initialize result data
                        Intent intent = result.getData();
                        // check condition
                        if(intent != null){
                            // when result data is not equal to empty
                            try{
                                Uri uri = intent.getData();
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(),uri);
                                // set bitmap on image view
                                photo_imageView.setImageBitmap(bitmap);
                                addedPhoto = true;

                                imageFilePath = getPathFromURI(HabitEventEditActivity.this, uri);
                                passedEvent.setLocalImagePath(imageFilePath);
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initialize intent
                Intent intent = new Intent(Intent.ACTION_PICK);
                // set type
                intent.setType("image/*");
                // launch in intent
                resultLauncher2.launch(intent);

            }
        });



//----------------------------------------------Location Information Process---------------------------------------------------------------------------
        // Reference: https://www.youtube.com/watch?v=t8nGh4gN1Q0
        // and https://www.youtube.com/watch?v=qO3FFuBrT2E for onActivityResult is Deprecated
        // Implement Autocomplete Place Api

        location_editText = findViewById(R.id.habitEvent_enterLocation_editText);

        // initialize place
        Places.initialize(getApplicationContext(),"AIzaSyCJvvbjw-Qdfxe_fwAnE9HwVFE9SelWUP0");
        PlacesClient placesClient = Places.createClient(this);

        // get current location https://www.youtube.com/watch?v=Ak1O9Gip-pg
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        currentLocation_button = findViewById(R.id.habitEvent_currentLocation_button);
        currentLocation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check permission
                if(ActivityCompat.checkSelfPermission(HabitEventEditActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                    // when permission granted

                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            // initialize location
                            Location location = task.getResult();
                            if(location != null){
                                try{
                                    // initialize getCoder
                                    Geocoder geocoder = new Geocoder(HabitEventEditActivity.this, Locale.getDefault());
                                    // initialize address list
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),location.getLongitude(),1

                                    );
                                    location_editText.setText(Html.fromHtml(addresses.get(0).getAddressLine(0)));
                                } catch(IOException e1){
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }
                else{
                    // when permission denied
                    ActivityCompat.requestPermissions(HabitEventEditActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    //when success
                    // initial place
                    Place place = Autocomplete.getPlaceFromIntent(result.getData());
                    // set address on edit text
                    location_editText.setText(place.getAddress());
                    // set locally name
                    // location_information.setText(String.format("Location is %s", place.getName()));
                }
                else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    // when have error
                    // initialize status
                    Status status = Autocomplete.getStatusFromIntent(result.getData());

                    // display toast
                    Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        // set edit text non focusable
        location_editText.setFocusable(false);
        View.OnClickListener locationEditTextListener = new LocationEditTextListener(this, activityResultLauncher);
        location_editText.setOnClickListener(locationEditTextListener);
    }

    /**
     * This function makes sure that when returning to habit event list using the arrow in the tool bar, a mode string will be passed
     * so that the return can be successful
     * This is how we customize the return arrow in the toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intentReturn = new Intent(getApplicationContext(), HabitEventListActivity.class); // Return to the habit event list page
                intentReturn.putExtra("StartMode", "normal");
                startActivity(intentReturn);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * get result from requesting permission to access external storage from user
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("---------------------> access granted!");
                }  else {
                    System.out.println("---------------------> request access failed!");
                }
                return;
        }
    }





//-----------------------------------------------functional APIs-------------------------------------------------------------------------------------------------------------

    // The following two methods took reference from: https://www.youtube.com/watch?v=-sItRxJ3rVk

    /**
     * This function extracts the on-device path of a picture from its uri
     * @param context
     * @param uri picture's uri
     * @return
     */
    public String getPathFromURI(Context context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

    /**
     * This path is used to verify whether we have permission to modify storage data locally
     * @param activity current activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * This function is used to upload an image to the firebase storage
     * This can only be called in the onCLickListener of confirm button
     * If the upload is successful, we get the url of the uploaded image and upload new event imformation to real time storage
     * This takes care of concurrency problem
     * @param imagePath
     */
    public static void uploadImage(String imagePath, String habitEventUUID, HabitEvent passedEvent, int eventIndexInList, ProgressDialog editEventProgressDialog, Activity activity, Context context, String uid) {

        String imageName = habitEventUUID+".jpg"; // Generate image name: habit_event_name.jpg

        Uri uri = Uri.fromFile(new File(imagePath));

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/users/"+ uid + "/" + imageName); // get storage reference
        storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("-----------------> Get photo url success! URL: " + uri.toString());

                        passedEvent.setDownloadUrl(uri.toString()); // Add photo url to the habit event for further use in retrieval

                        // Upload information to real time database
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(passedEvent.getUuid(),passedEvent);
                        FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent").updateChildren(map);

                        // shift back to list activity
                        Intent intentReturn = new Intent(context, HabitEventListActivity.class); // Return to the habit event list page
                        intentReturn.putExtra("StartMode", "Edit");
                        intentReturn.putExtra("EventIndex", eventIndexInList);
                        intentReturn.putExtra("HabitEventFromEdit", passedEvent);

                        activity.startActivity(intentReturn);
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

    /**
     * This function is used to load an image to photo_imageView using the image's url
     * @param uri the saved url
     */
    public void loadUrlImageToImageView(Uri uri) {
        // More on glide library: https://github.com/bumptech/glide
        Glide.with(this).load(uri).into(photo_imageView);
    }

    /**
     * This function is used to delete the habit event from the list stored in corresponding habit
     * @param habitName
     * @param eventUUID
     */
    public static void deleteEventFromHabit(String habitName, String eventUUID, String uid) {
        DatabaseReference habitRef = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit");
        habitRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Habit habitE = (Habit) dataSnapshot.getValue(Habit.class);
                    // Determine whether we are processing the right habit
                    if (habitE.getHabitTitle().equals(habitName)) {
                        ArrayList<String> habitEventNameList = habitE.getEventList();
                        habitEventNameList.remove(eventUUID);
                        habitE.setEventList(habitEventNameList);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put(habitE.getHabitTitle(),habitE);
                        FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}