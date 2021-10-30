package com.example.habittracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class HabitEventEditActivity extends AppCompatActivity implements DeleteConfirmFragment.OnDeleteConfirmFragmentInteractionListener {
    EditText location_editText;
    TextView location_information;
    EditText comment_editText;
    Button deleteBtn;
    Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_edit);

        // Initial setup for activity------------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        getSupportActionBar().setTitle("Habit Event - Edit");
        // set return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deleteBtn = findViewById(R.id.habitEvent_delete_button);
        confirmBtn = findViewById(R.id.habitEvent_confirm_button);
        location_editText = findViewById(R.id.habitEvent_enterLocation_editText);
        location_information = findViewById(R.id.habitEvent_locationInfo_textView);
        comment_editText = findViewById(R.id.habitEvent_comment_editText);


        //------------------------------------------------------------------------------------------------------------------------------------


        // Set on-click listener for all buttons------------------------------------------------------------------------------------------------

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteConfirmFragment("Are you sure you want to delete?").show(getSupportFragmentManager(), "DELETE_HABIT_EVENT");
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = comment_editText.getText().toString();
                String name = "Habit name"; //TODO: generate habit event name by habit name and date
                HabitEvent newEvent = new HabitEvent(name, comment);  // Comment can be empty, hence no error checking
                //TODO: pass this habit event to the habit event list


                Intent intentReturn = new Intent(getApplicationContext(), HabitEventListActivity.class); // Return to the habit event list page
                intentReturn.putExtra("habit event", newEvent);
                startActivity(intentReturn);
                finish(); // finish current activity
            }
        });

        //------------------------------------------------------------------------------------------------------------------------------------

        //-----------------------------Location Information Process---------------------------------------------------------------------------

        // Reference: https://www.youtube.com/watch?v=t8nGh4gN1Q0
        // Implement Autocomplete Place Api

        // initialize place
       Places.initialize(getApplicationContext(),"AIzaSyCJvvbjw-Qdfxe_fwAnE9HwVFE9SelWUP0");
        PlacesClient placesClient = Places.createClient(this);


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

                    // stat=rt activity result
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    // TODO: Handle the error.
                    Log.e("error", e.getMessage());
                    e.printStackTrace();
                }


            }
        });

        //--------------------------------------------------------------------------------------------------------------------------------------
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && requestCode == RESULT_OK) {
            //when success
            // initial place
            Place place = Autocomplete.getPlaceFromIntent(data);
            // set address on edit text
            location_editText.setText(place.getAddress());
            // set locally name
            location_information.setText(String.format("Location is %s", place.getName()));
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // when have error
            // initialize status
            Status status = Autocomplete.getStatusFromIntent(data);

            // display toast
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void onConfirmDeletePressed() {
        return;
    }


}