//package com.example.habittracker.listener;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.text.Html;
//import android.view.View;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//
//import com.example.habittracker.HabitEventEditActivity;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class CurrentLocationListener implements View.OnClickListener{
//    Activity activity;
//    FusedLocationProviderClient fusedLocationProviderClient;
//
//    EditText location_editText;
//
//    public CurrentLocationListener(Activity activity, EditText location_editText) {
//        this.activity = activity;
//        this.location_editText = location_editText;
//        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
//    }
//
//    @Override
//    public void onClick(View view) {
//        // check permission
//        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
//            // when permission granted
//
//            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    // initialize location
//                    Location location = task.getResult();
//                    if(location != null){
//                        try{
//                            // initialize getCoder
//                            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
//                            // initialize address list
//                            List<Address> addresses = geocoder.getFromLocation(
//                                    location.getLatitude(),location.getLongitude(),1
//
//                            );
//                            location_editText.setText(Html.fromHtml(addresses.get(0).getAddressLine(0)));
//                        } catch(IOException e1){
//                            e1.printStackTrace();
//                        }
//                    }
//                }
//            });
//        }
//        else{
//            // when permission denied
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }
//    }
//}
