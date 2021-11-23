//package com.example.habittracker.listener;
//
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.widget.Toast;
//
//import com.example.habittracker.HabitEventEditMapActivity;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class LocationConfirmListener implements OnSuccessListener {
//    @Override
//    public void onSuccess(Location location) {
//
//        // when success
//
//        if (location != null){
//
//
//            // sync map
//            mapFragment.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap googleMap) {
//                    mMap = googleMap;
//
//
//
//                    Geocoder geocoder = new Geocoder(HabitEventEditMapActivity.this, Locale.getDefault());
//
//                    List<Address> c_addresses = null;
//                    try{
//                        c_addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//
//                    }catch (IOException e){
//                        e.printStackTrace();
//
//                    }
//                    if (c_addresses != null){
//                        currentAddress = c_addresses.get(0).getAddressLine(0);
//                    }
//
//
//                    // initial lat lng
//
//
//                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//                    // create mark option
//
//                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here!");
//                    choose_location_info.setText(currentAddress);
//
//
//                    // zoom map
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
//
//                    // add marker on map
//                    googleMap.addMarker(markerOptions).showInfoWindow();
//
//                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                        @Override
//                        public void onMapClick(LatLng latLng) {
//                            CheckConnection();
//                            if(networkInfo.isConnected() && networkInfo.isAvailable()){
//
//                                selectedLat = latLng.latitude;
//                                selectedLng = latLng.longitude;
//
//                                GetAddress(selectedLat,selectedLng);
//                            }else{
//                                Toast.makeText(HabitEventEditMapActivity.this, "Please check connection", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//
//
//                }
//            });
//        }else{
//            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//                    CheckConnection();
//                    if(networkInfo.isConnected() && networkInfo.isAvailable()){
//
//                        selectedLat = latLng.latitude;
//                        selectedLng = latLng.longitude;
//
//                        GetAddress(selectedLat,selectedLng);
//                    }else{
//                        Toast.makeText(HabitEventEditMapActivity.this, "Please check connection", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//        }
//
//    }
//}
