package com.example.habittracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HabitEventEditActivity extends AppCompatActivity implements DeleteConfirmFragment.OnDeleteConfirmFragmentInteractionListener {
    EditText location_editText;
    EditText comment_editText;
    Button photo_button;
    ImageView photo_imageView;
    ActivityResultLauncher<Intent> activityResultLauncher;

    Button currentLocation_button;
    Button deleteBtn;
    Button confirmBtn;
    FusedLocationProviderClient fusedLocationProviderClient;

    HabitEvent passedEvent;
    HabitEvent newEvent;
    String habitName;
    String imageFilePath; // This always saves the latest uri of the image shown in event
    int eventIndexInList;

    private static int REQUEST_CODE = 100;
    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_edit);

        Intent intent = getIntent();

        deleteBtn = findViewById(R.id.habitEvent_delete_button);
        confirmBtn = findViewById(R.id.habitEvent_confirm_button);
        location_editText = findViewById(R.id.habitEvent_enterLocation_editText);
        comment_editText = findViewById(R.id.habitEvent_comment_editText);
        photo_imageView = findViewById(R.id.habitEvent_photo_imageView);

        //Get passed habit event object from other events------------------------------------------------------------------------------------------------
        // And display information (if any)
        Bundle data = intent.getExtras();
        eventIndexInList = data.getInt("EventIndex");

        if (eventIndexInList >= 0) {
            // In this case we are editing an entry in the list
            passedEvent = (HabitEvent) data.getParcelable("HabitEventForEdit");
            comment_editText.setText(passedEvent.getComment());
            location_editText.setText(passedEvent.getLocation());

            // load image
            imageFilePath = passedEvent.getImageName();
            Uri photo_uri = Uri.parse(imageFilePath);

            try {





                Bitmap bitmap = null;


                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photo_uri);
                photo_imageView.setImageBitmap(bitmap);
                Toast.makeText(HabitEventEditActivity.this, "pic good", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(HabitEventEditActivity.this, "no pic", Toast.LENGTH_SHORT).show();
            }



//            File dir = new File(Environment.getExternalStorageDirectory(), "SavedImage");
//            File image = new File(dir, passedEvent.getImageName());
//            Bitmap imageBitMap = BitmapFactory.decodeFile(image.getAbsolutePath());
//            photo_imageView.setImageBitmap(imageBitMap);
        }
        else {
            // In this case we are adding a new event
            habitName = data.getString("HabitName"); //TODO: use this on the habit side to transfer data
        }




        getSupportActionBar().setTitle("Habit Event - Edit");


        Button deleteBtn = findViewById(R.id.habitEvent_delete_button);
        Button confirmBtn = findViewById(R.id.habitEvent_confirm_button);
        // set return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteConfirmFragment("Are you sure you want to delete?").show(getSupportFragmentManager(), "DELETE_HABIT_EVENT");
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReturn = new Intent(getApplicationContext(), HabitEventListActivity.class); // Return to the habit event list page

                intentReturn.putExtra("StartMode", "Edit");

                String comment = comment_editText.getText().toString();
                String location = location_editText.getText().toString();
                String imageName = imageFilePath;

                if (eventIndexInList >= 0) {
                    // modify current entry
                    passedEvent.setComment(comment);
                    passedEvent.setLocation(location);
                    passedEvent.setImageName(imageName);
//                    if (ContextCompat.checkSelfPermission(HabitEventEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        System.out.println("66666666666666666666666666");
////                        saveImage(passedEvent);
//                    }
//                    else {
//                        System.out.println("77777777777777777777777777");
////                        askPermission();
//                    }
                    intentReturn.putExtra("EventIndex", eventIndexInList);
                    intentReturn.putExtra("HabitEventFromEdit", passedEvent);
                }
                else {
                    // create new entry
                    newEvent = new HabitEvent(habitName, comment, location, habitName+System.currentTimeMillis()+".jpg");  // Comment can be empty, hence no error checking
//                    saveImage(newEvent);
                    intentReturn.putExtra("EventIndex", eventIndexInList);
                    intentReturn.putExtra("HabitEventFromEdit", newEvent);
                }

                startActivity(intentReturn);
                finish(); // finish current activity
            }
        });




        // Add photo to event------------------------------------------------------------------------------------------------------------------------------------
        // photo part
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
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(),intent.getData());
                                // set bitmap on image view
                                photo_imageView.setImageBitmap(bitmap);

                                imageFilePath = intent.getData().toString();


                            }catch (IOException e){
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



        //-----------------------------Location Information Process---------------------------------------------------------------------------
        // location part

        // location part
        location_editText = findViewById(R.id.habitEvent_enterLocation_editText);


    // Reference: https://www.youtube.com/watch?v=t8nGh4gN1Q0
//        and https://www.youtube.com/watch?v=qO3FFuBrT2E for onActivityResult is Deprecated
        // Implement Autocomplete Place Api

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
                }else{
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
//                    // set locally name
//                    location_information.setText(String.format("Location is %s", place.getName()));
                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
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
        location_editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

                try{
                    // Create intent
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fieldList).build(HabitEventEditActivity.this);

                    // start activity result

//                    startActivityForResult(intent, 100);
                    activityResultLauncher.launch(intent);


                } catch (Exception e) {
                    // TODO: Handle the error.
                    Log.e("error", e.getMessage());
                    e.printStackTrace();
                }


            }
        });

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_CODE)
//        {
//            System.out.println("9999999999999999999999999999");
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                if (eventIndexInList >= 0) {
//                    saveImage(passedEvent);
//                }
//                else{
//                    saveImage(newEvent);
//                }
//            }else {
//                Toast.makeText(HabitEventEditActivity.this,"Please provide the required permissions",Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    private void askPermission() {
//        System.out.println("88888888888888888888888888888");
//        ActivityCompat.requestPermissions(HabitEventEditActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
//    }



    public void onConfirmDeletePressed() {
        return;
    }

//    public void saveImage(HabitEvent event) {
//        if (photo_imageView.getDrawable() == null) {
//            System.out.println("No image is shown currently");
//            return;
//        }
//
//        // Save file
//        File dir = new File(Environment.getExternalStorageDirectory(), "SavedImage");
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//
//        BitmapDrawable drawable = (BitmapDrawable) photo_imageView.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//
//        File file = new File(dir, event.getImageName()); // Save image as the image name provided in the SavedImage folder
//
//        try {
//            outputStream = new FileOutputStream(file);
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
//
//        try {
//            outputStream.flush();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("99999999999999999999999999999");
//    }
//

}