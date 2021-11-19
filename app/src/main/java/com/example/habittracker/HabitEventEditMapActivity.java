//package com.example.habittracker;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.os.PatternMatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class HabitEventEditMapActivity extends AppCompatActivity {
//
////
////    private FusedLocationProviderClient client;
////    private SupportMapFragment mapFragment;
////    private int REQUEST_CODE = 111;
////    private ConnectivityManager manager;
////    private NetworkInfo networkInfo;
////    private GoogleMap mMap;
////    private Geocoder geocoder;
////    private double selectedLat, selectedLng;
////
////    private List<Address> addresses;
////    private String selectedAddress;
////
////    private List<Address> c_addresses;
////    private String currentAddress;
////
////    private LocationManager locMan;
////
////
////    TextView choose_location_info;
////    Button confirm_choose_location_button;
////
////
////    boolean click_confirm;
//
//
//    // Reference: https://www.youtube.com/watch?v=fpQQAUAqSjM
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_habit_event_map);
//
////        choose_location_info = findViewById(R.id.map_textView);
////        confirm_choose_location_button = findViewById(R.id.map_conform_button);
////
////
////        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////
////
////
////
//////         set return arrow
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////
////
////
////        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
////        client = LocationServices.getFusedLocationProviderClient(HabitEventEditMapActivity.this);
////
////
////        if (ActivityCompat.checkSelfPermission(HabitEventEditMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
////                == PackageManager.PERMISSION_GRANTED) {
////            getLocation();
////            System.out.println("no 9999999999999999999999");
////
////        } else {
////            // when permission denied
////            // request permission
////            ActivityCompat.requestPermissions(HabitEventEditMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
////
////
////
////        }
////
////        Intent i = new Intent(HabitEventEditMapActivity.this, HabitEventEditActivity.class);
////
////
////
////
////        // set confirm button
////        confirm_choose_location_button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                click_confirm = true;
////
////                String the_value = choose_location_info.getText().toString();
////
////
////
////                i.putExtra("Location_Value",  the_value);
////                i.putExtra("If_Confirm", true);
////                System.out.println(the_value);
////
////                startActivity(i);
////                finish();
////
////            }
////        });
//
//
//
//
//
//
//
//
//
//    }
//
//
//
////
////
////    private void getLocation() {
////
////        if (ActivityCompat.checkSelfPermission(HabitEventEditMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            System.out.println("no fffffffffffffffffffffffffffffff");
////            ActivityCompat.requestPermissions(HabitEventEditMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
////            ActivityCompat.requestPermissions(HabitEventEditMapActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},44);
////            return;
////        }else{
////            System.out.println("no ggggggggggggggggggggggggggggggg");
////
////
////            // get current location
////            // initial task location
////            Task<Location> task = client.getLastLocation();
////
////            task.addOnSuccessListener(new OnSuccessListener<Location>() {
////                @Override
////                public void onSuccess(Location location) {
////
////                    // when success
////                    System.out.println("no aaaaaaaaaaaaaaaaaaaaaaaaa");
////                    ///////// should change!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////                    if (location != null){
////                        System.out.println("no bbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
////
////                        // sync map
////                        mapFragment.getMapAsync(new OnMapReadyCallback() {
////                            @Override
////                            public void onMapReady(GoogleMap googleMap) {
////                                mMap = googleMap;
////
////
////
////                                Geocoder geocoder = new Geocoder(HabitEventEditMapActivity.this, Locale.getDefault());
////
////                                List<Address> c_addresses = null;
////                                try{
////                                    c_addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
////
////                                }catch (IOException e){
////                                    e.printStackTrace();
////
////                                }
////                                if (c_addresses != null){
////                                    currentAddress = c_addresses.get(0).getAddressLine(0);
////                                }
////
////
////                                // initial lat lng
////
////
////                                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
////                                // create mark option
////
////                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here!");
////                                choose_location_info.setText(currentAddress);
////
////
////                                // zoom map
////                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
////
////                                // add marker on map
////                                googleMap.addMarker(markerOptions).showInfoWindow();
////
////                                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
////                                    @Override
////                                    public void onMapClick(LatLng latLng) {
////                                        CheckConnection();
////                                        if(networkInfo.isConnected() && networkInfo.isAvailable()){
////
////                                            selectedLat = latLng.latitude;
////                                            selectedLng = latLng.longitude;
////
////                                            GetAddress(selectedLat,selectedLng);
////                                        }else{
////                                            Toast.makeText(HabitEventEditMapActivity.this, "Please check connection", Toast.LENGTH_SHORT).show();
////                                        }
////                                    }
////                                });
////
////
////
////                            }
////                        });
////                    }else{
////                        System.out.println("no dddddddddddddddddddddddddddddddddd");
////
////
////
////                        // sync map
////                        mapFragment.getMapAsync(new OnMapReadyCallback() {
////                            @Override
////                            public void onMapReady(GoogleMap googleMap) {
////                                mMap = googleMap;
////
////                                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
////                                    @Override
////                                    public void onMapClick(LatLng latLng) {
////                                        CheckConnection();
////                                        if(networkInfo.isConnected() && networkInfo.isAvailable()){
////
////                                            selectedLat = latLng.latitude;
////                                            selectedLng = latLng.longitude;
////
////                                            GetAddress(selectedLat,selectedLng);
////                                        }else{
////                                            Toast.makeText(HabitEventEditMapActivity.this, "Please check connection", Toast.LENGTH_SHORT).show();
////                                        }
////                                    }
////                                });
////
////
////
////                            }
////                        });
////                    }
////
////                }
////            });
////
////        }
////
////
////
////    }
////
////
////
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permission, grantResults);
////        if (requestCode == REQUEST_CODE) {
////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                System.out.println("no ccccccccccccccccccccccccccccccccccccccc");
////                getLocation();
////            }
////        } else {
////            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
////        }
////    }
////
////
////
////
////
////
////
////    private void CheckConnection(){
////        manager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
////        networkInfo = manager.getActiveNetworkInfo();
////    }
////
////    private void GetAddress(double mLat, double mLng){
////        geocoder = new Geocoder(HabitEventEditMapActivity.this, Locale.getDefault());
////        if(mLat != 0){
////            try{
////                addresses = geocoder.getFromLocation(mLat,mLng,1);
////
////            }catch (IOException e){
////                e.printStackTrace();
////            }
////            if(addresses != null){
////                String mAddress = addresses.get(0).getAddressLine(0);
////
////                String city = addresses.get(0).getLocality();
////                String state = addresses.get(0).getAdminArea();
////                String country = addresses.get(0).getCountryName();
////                String position = addresses.get(0).getPostalCode();
////                String knownName = addresses.get(0).getFeatureName();
////                String dis = addresses.get(0).getSubAdminArea();
////
////                selectedAddress = mAddress;
////
////                if(mAddress != null){
////                    MarkerOptions markerOptions = new MarkerOptions();
////
////                    LatLng latLng = new LatLng(mLat,mLng);
////                    markerOptions.position(latLng).title(selectedAddress);
////
////
////                    choose_location_info.setText(selectedAddress);
////
////
////                    mMap.addMarker(markerOptions).showInfoWindow();
////
////
////                }else{
////                    Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show();
////                }
////
////
////            }else{
////                Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show();
////            }
////        }else{
////            Toast.makeText(this,"LatLng null",Toast.LENGTH_SHORT).show();
////        }
////    }
//
//}
