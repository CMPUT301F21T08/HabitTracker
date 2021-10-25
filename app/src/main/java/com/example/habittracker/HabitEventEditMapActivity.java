package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

// Reference: https://www.youtube.com/watch?v=t8nGh4gN1Q0
public class HabitEventEditMapActivity extends AppCompatActivity {
    EditText location_editText;
    TextView location_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location_editText = findViewById(R.id.habitEvent_enterLocation_editText);
        location_information = findViewById(R.id.habitEvent_locationInfo_textView);


        // initialize place
        Places.initialize(getApplicationContext(),"AIzaSyBNWUxVi9YbT9JV813uxfe64Y-5AxtO46E");

        // set edit text non focusable
        location_editText.setFocusable(false);
        location_editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

                // Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(HabitEventEditMapActivity.this);

                // stat=rt activity result
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode, data);
        if (requestCode  == 100 && requestCode == RESULT_OK){
            //when success
            // initial place
            Place place = Autocomplete.getPlaceFromIntent(data);
            // set address on edit text
            location_editText.setText(place.getAddress());
            // set locally name
            location_information.setText(String.format("Location is %s", place.getName()));
        }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            // when have error
            // initialize status
            Status status = Autocomplete.getStatusFromIntent(data);

            // display toast
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }
    }


}