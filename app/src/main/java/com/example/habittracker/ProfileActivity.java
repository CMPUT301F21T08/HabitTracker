package com.example.habittracker;

import android.content.Intent;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * @author 'zwan1'
 * Shows the user's profile information and allowing users to edit them, also provides the functionality of Signing Out
 * User Name: shown as an EditText that can be edited
 * User Email: shown as a TextView that cannot be edited
 * Gender: shown as an EditText that can be edited, can only enter "Male", "Female" or "Others"
 * Age: shown as an EditText that can be edited, can only enter integer from 0 to 100
 */
public class ProfileActivity extends AppCompatActivity {

    Button signOut_btn;
    Button applyChanges_btn;
    BottomNavigationView bottomNavigationView;
    EditText userName;
    TextView userEmail;
    EditText userGender;
    EditText userAge;
    private FirebaseAuth authentication;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        
        // The the corresponding user unique uid, in order to locate into the correct user branch in firebase
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }
        
        // connecting attributes in the layout file
        userName = findViewById(R.id.profile_userName_EditText);
        userEmail = findViewById(R.id.profile_userEmail_TextView);
        userGender = findViewById(R.id.profile_userGender_EditText);
        userAge = findViewById(R.id.profile_userAge_EditText);


        // Using the user uid to get the correct branch for this user, go into the "Info" branch to fetch user information
        // Information stored as an Personal_info class in the firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Personal_info info = (Personal_info) dataSnapshot.getValue(Personal_info.class);
                if (info != null) {
                    userName.setText(info.getName());
                    userEmail.setText(info.getEmail());
                    userGender.setText(info.getGender());
                    userAge.setText(Integer.toString(info.getAge()));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        
        // Create a button that lets user to sign out and go back to the login page
        signOut_btn = findViewById(R.id.profile_signOut_button);
        signOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Sign Out successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, LogInActivity.class));
            }
        });

        // Create a button that apply the changes that user made to either "User Name", "Gender" or "Age"
        applyChanges_btn = findViewById(R.id.profile_change_button);
        applyChanges_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userGender.getText().toString().trim().compareTo("Male") != 0 && userGender.getText().toString().trim().compareTo("Female") != 0 && userGender.getText().toString().trim().compareTo("Others") != 0){
                    Toast.makeText(ProfileActivity.this, "Wrong Input for Gender, please enter Male, Female or Others", Toast.LENGTH_SHORT).show();
                } else if (100<=Integer.parseInt(userAge.getText().toString().trim()) || Integer.parseInt(userAge.getText().toString().trim()) <0 ){
                    Toast.makeText(ProfileActivity.this, "Wrong Input for Age, please enter an integer between 0 to 100", Toast.LENGTH_SHORT).show();
                } else{
                    HashMap<String,Object> map = new HashMap<>();
                    Personal_info personal_info = new Personal_info(userName.getText().toString().trim(),userEmail.getText().toString(),userGender.getText().toString().trim(),Integer.parseInt(userAge.getText().toString().trim()) );
                    map.put("Info",personal_info);
                    FirebaseDatabase.getInstance().getReference().child(uid).updateChildren(map);
                    Toast.makeText(ProfileActivity.this, "Changes has been made!", Toast.LENGTH_SHORT).show();
                }
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
