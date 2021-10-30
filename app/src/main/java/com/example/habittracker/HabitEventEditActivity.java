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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HabitEventEditActivity extends AppCompatActivity implements DeleteConfirmFragment.OnDeleteConfirmFragmentInteractionListener {
    EditText location_editText;
    Button photo_button;
    ImageView photo_imageView;
    ActivityResultLauncher<Intent> activityResultLauncher;

    Button currentLocation_button;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_edit);








        getSupportActionBar().setTitle("Habit Event - Edit");

//        Intent intent = getIntent();

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
                startActivity(intentReturn);
                finish(); // finish current activity
            }
        });




// photo part
// reference: https://www.youtube.com/watch?v=HxlAktedIhM
        photo_button = findViewById(R.id.habitEvent_addPhoto_button);
        photo_imageView = findViewById(R.id.habitEvent_photo_imageView);

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
                                        getContentResolver(),intent.getData()
                                );
                                // set bitmap on image view
                                photo_imageView.setImageBitmap(bitmap);

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
//    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && requestCode == RESULT_OK) {
//            //when success
//            // initial place
//            Place place = Autocomplete.getPlaceFromIntent(data);
//            // set address on edit text
//            location_editText.setText(place.getAddress());
//            // set locally name
//            location_information.setText(String.format("Location is %s", place.getName()));
//        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//            // when have error
//            // initialize status
//            Status status = Autocomplete.getStatusFromIntent(data);
//
//            // display toast
//            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
//
//            location_editText.setText("Location is %s");
//            // set locally name
//            location_information.setText("Location is %s");
//        }
//    }


    public void onConfirmDeletePressed() {
        return;
    }


}