package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HabitEventEditMapActivity extends AppCompatActivity {


    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private int REQUEST_CODE = 111;
    private ConnectivityManager manager;
    private NetworkInfo networkInfo;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private double selectedLat, selectedLng;

    private List<Address> addresses;
    private String selectedAddress;

    private List<Address> c_addresses;
    private String currentAddress;

    private LocationManager locMan;

    private boolean hasLocation = false;


    TextView choose_location_info;
    Button confirm_choose_location_button;
    Button return_choose_location_button;


    // Reference: https://www.youtube.com/watch?v=fpQQAUAqSjM

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_map);

        choose_location_info = findViewById(R.id.map_textView);
        confirm_choose_location_button = findViewById(R.id.map_conform_button);
        return_choose_location_button = findViewById(R.id.map_return_button);


        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        client = LocationServices.getFusedLocationProviderClient(HabitEventEditMapActivity.this);


        if (ActivityCompat.checkSelfPermission(HabitEventEditMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();

        } else {
            // when permission denied
            // request permission
            ActivityCompat.requestPermissions(HabitEventEditMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);


        }
        // set confirm button
        confirm_choose_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Location_Value", choose_location_info.getText().toString());
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        // set return button
        return_choose_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();


            }
        });
    }

    /**
     * Process the KEY_RETURN signal in the map activity
     * Make it having the same effect as clicking the return button
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    /**
     * This gets current location and shows marker and address.
     * After that when user clicks on the map it shows marker and address.
     */
    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(HabitEventEditMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HabitEventEditMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            ActivityCompat.requestPermissions(HabitEventEditMapActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
            return;
        }
        else {

            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(60000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            //TODO: UI updates.
                            hasLocation = true;
                        }
                    }

                    if (hasLocation) {
                        if (ActivityCompat.checkSelfPermission(HabitEventEditMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HabitEventEditMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        LocationServices.getFusedLocationProviderClient(HabitEventEditMapActivity.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                // when success
                                if (location != null) {

                                    // sync map
                                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {
                                            mMap = googleMap;


                                            Geocoder geocoder = new Geocoder(HabitEventEditMapActivity.this, Locale.getDefault());

                                            List<Address> c_addresses = null;
                                            try {
                                                c_addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                            } catch (IOException e) {
                                                e.printStackTrace();

                                            }
                                            if (c_addresses != null) {
                                                currentAddress = c_addresses.get(0).getAddressLine(0);
                                            }


                                            // initial lat lng


                                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                            // create mark option

                                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here!");
                                            choose_location_info.setText(currentAddress);


                                            // zoom map
                                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                                            // add marker on map
                                            googleMap.addMarker(markerOptions).showInfoWindow();

                                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                                @Override
                                                public void onMapClick(LatLng latLng) {
                                                    CheckConnection();
                                                    if (networkInfo.isConnected() && networkInfo.isAvailable()) {

                                                        selectedLat = latLng.latitude;
                                                        selectedLng = latLng.longitude;

                                                        GetAddress(selectedLat, selectedLng);
                                                    } else {
                                                        Toast.makeText(HabitEventEditMapActivity.this, "Please check connection", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                        }
                                    });
                                } else {
                                    // sync map
                                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {
                                            mMap = googleMap;

                                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                                @Override
                                                public void onMapClick(LatLng latLng) {
                                                    CheckConnection();
                                                    if (networkInfo.isConnected() && networkInfo.isAvailable()) {

                                                        selectedLat = latLng.latitude;
                                                        selectedLng = latLng.longitude;

                                                        GetAddress(selectedLat, selectedLng);
                                                    } else {
                                                        Toast.makeText(HabitEventEditMapActivity.this, "Please check connection", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            };
            LocationServices.getFusedLocationProviderClient(HabitEventEditMapActivity.this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        }



    }

    /**
     * Check permission.
     * @param requestCode
     *   This is the request code passed in.
     * @param permission
     *   This is the requested permissions.
     * @param grantResults
     *   The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED.
     */


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Check internet connection.
     */

    private void CheckConnection(){
        manager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
    }

    /**
     * Get address of a location.
     * @param mLat
     *   This is latitude of address.
     * @param mLng
     *   This is longitude of address.
     */

    private void GetAddress(double mLat, double mLng){
        geocoder = new Geocoder(HabitEventEditMapActivity.this, Locale.getDefault());
        if(mLat != 0){
            try{
                addresses = geocoder.getFromLocation(mLat,mLng,1);

            }catch (IOException e){
                e.printStackTrace();
            }
            if(addresses != null){
                String mAddress = addresses.get(0).getAddressLine(0);

                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String position = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String dis = addresses.get(0).getSubAdminArea();

                selectedAddress = mAddress;

                if(mAddress != null){
                    MarkerOptions markerOptions = new MarkerOptions();

                    LatLng latLng = new LatLng(mLat,mLng);
                    markerOptions.position(latLng).title(selectedAddress);


                    choose_location_info.setText(selectedAddress);


                    mMap.addMarker(markerOptions).showInfoWindow();


                }else{
                    Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show();
                }


            }else{
                Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"LatLng null",Toast.LENGTH_SHORT).show();
        }
    }

}
