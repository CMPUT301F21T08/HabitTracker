package com.example.habittracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    Button addImage_btn;
    ImageView profile_Image;
    BottomNavigationView bottomNavigationView;
    EditText userName;
    EditText userEmail;
    EditText userAge;
    String imageFilePath;
    private FirebaseAuth authentication;
    private String uid;
    boolean addedPhoto;
    private RadioGroup gender_RadioGroup;
    private RadioButton gender_RadioButton;


    private final String FEMALE = "Female";
    private final String MALE = "Male";
    private final String OTHER = "Other";

    static Personal_info personal_info;
    Uri uri;

    public ProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        boolean addedPhoto = false;  // flag that is used to determine whether user has added a photo

        gender_RadioGroup = findViewById(R.id.profile_gender_RadioGroup);
        gender_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                gender_RadioButton = findViewById(i);
            }
        });


        // The corresponding user unique uid, in order to locate into the correct user branch in firebase
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }


        // connecting attributes in the layout file
        userName = findViewById(R.id.profile_userName_EditText);
        userEmail = findViewById(R.id.profile_userEmail_EditText);
        //  userGender = findViewById(R.id.profile_userGender_EditText);
        userAge = findViewById(R.id.profile_userAge_EditText);

        // add image
        profile_Image = findViewById(R.id.profile_ImageView);
        addImage_btn = findViewById(R.id.profile_addImage_Button);


        ActivityResultLauncher<Intent> resultLauncher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // initialize result data
                        Intent intent = result.getData();
                        // check condition
                        if (intent != null) {
                            // when result data is not equal to empty
                            try {
                                uri = intent.getData();
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(), uri);
                                // set bitmap on image view
                                profile_Image.setImageBitmap(bitmap);

                                imageFilePath = getPathFromURI(ProfileActivity.this, uri);
                                personal_info.setLocalImagePath(imageFilePath);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        addImage_btn.setOnClickListener(new View.OnClickListener() {
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


        // Using the user uid to get the correct branch for this user, go into the "Info" branch to fetch user information
        // Information stored as an Personal_info class in the firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personal_info = (Personal_info) dataSnapshot.getValue(Personal_info.class);
                if (personal_info != null) {
                    userName.setText(personal_info.getName());
                    userEmail.setText(personal_info.getEmail());
                    if (personal_info.getGenderId() != -1){
                        gender_RadioGroup.check(personal_info.getGenderId());
                    }
                    userAge.setText(Integer.toString(personal_info.getAge()));
                    if (personal_info.getDownloadUrl() != null){
                        Uri uri = Uri.parse(personal_info.getDownloadUrl());
                        Glide.with(getApplicationContext()).load(uri).into(profile_Image);
                    }
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
                finish();
            }
        });



        // Create a button that apply the changes that user made to either "User Name", "Gender" or "Age"
        applyChanges_btn = findViewById(R.id.profile_change_button);
        applyChanges_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (100<=Integer.parseInt(userAge.getText().toString().trim()) || Integer.parseInt(userAge.getText().toString().trim()) <0 || gender_RadioButton == null){
                    Toast.makeText(ProfileActivity.this, "Wrong Input for Age, please enter an integer between 0 to 100", Toast.LENGTH_SHORT).show();
                } else if (personal_info.getLocalImagePath() != null){
                    personal_info = new Personal_info(userName.getText().toString().trim(),userEmail.getText().toString(),gender_RadioButton.getText().toString(), gender_RadioButton.getId(),Integer.parseInt(userAge.getText().toString().trim()), personal_info.getLocalImagePath());
                    personal_info.setUid(uid);


                    System.out.println(personal_info.getGender());

                    uploadImage(personal_info, uid);
                    Toast.makeText(ProfileActivity.this, "Changes has been made!", Toast.LENGTH_SHORT).show();
                } else{
                    HashMap<String,Object> map = new HashMap<>();
                    personal_info = new Personal_info(userName.getText().toString().trim(),userEmail.getText().toString(),gender_RadioButton.getText().toString(), gender_RadioButton.getId(),Integer.parseInt(userAge.getText().toString().trim()), personal_info.getLocalImagePath());
                    personal_info.setUid(uid);


                    System.out.println(personal_info.getGender());


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

    /**
     * Process the KEY_RETURN signal in profile activity
     * When back button is pressed, return to main page activity
     */
    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(ProfileActivity.this, MainPageActivity.class);
        startActivity(intent3);
        finish();
    }


    public String getPathFromURI(Context context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

    public static void uploadImage(Personal_info user, String uid) {

        String imageName = "profilePhoto.jpg"; // Generate image name: habit_event_name.jpg
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/users/"+ uid + "/" + imageName); // get storage reference
        Uri uri = Uri.fromFile(new File(user.getLocalImagePath()));
        storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("-----------------> Get photo url success! URL: " + uri.toString());
                        personal_info.setDownloadUrl(uri.toString());
                        HashMap<String, Object> map = new HashMap<>();
                        personal_info.setUid(uid);
                        map.put("Info", personal_info);
                        FirebaseDatabase.getInstance().getReference().child(uid).updateChildren(map);
                    }
                });
            }
        });
    }
}
