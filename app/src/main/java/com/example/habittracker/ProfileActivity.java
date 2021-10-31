package com.example.habittracker;

import android.content.Intent;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    Button signOut_btn;
    BottomNavigationView bottomNavigationView;
    TextView userName;
    TextView userEmail;
    private FirebaseAuth authentication;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }
        userName = findViewById(R.id.profile_userName_TextView);
        userEmail = findViewById(R.id.profile_userEmail_TextView);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    if (map != null) {
                        userName.setText(map.get("name"));
                        userEmail.setText(map.get("email"));
                    }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });


        signOut_btn = findViewById(R.id.profile_signOut_button);
        signOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Sign Out successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, LogInActivity.class));
            }
        });

        // Process Navigation Bar
        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_habit:
                        Intent intent1 = new Intent(ProfileActivity.this, HabitListActivity.class);
                        intent1.putExtra("StartMode", "normal");
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.navigation_habitEvent:
                        Intent intent2 = new Intent(ProfileActivity.this, HabitEventListActivity.class);
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.navigation_homePage:
                        Intent intent3 = new Intent(ProfileActivity.this, MainPageActivity.class);
                        startActivity(intent3);
                        finish();
                        return true;
                    case R.id.navigation_following:
                        Intent intent4 = new Intent(ProfileActivity.this, FollowingActivity.class);
                        startActivity(intent4);
                        finish();
                        return true;
                    case R.id.navigation_settings:
                        return true;
                }
                return false;
            }
        });
    }
}
