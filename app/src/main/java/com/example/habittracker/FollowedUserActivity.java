package com.example.habittracker;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FollowedUserActivity extends AppCompatActivity {
    private Button UnsubBtn;
    private TextView UserNameTextView;
    private TextView UserAgeTextView;
    private TextView UserGenderTextView;

    private ListView UserHabitsListView;
    private ImageView UserAvatarImageView;
    private String followed_user_uid;

    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID
    private Personal_info User;
    private ArrayAdapter<Habit> habitArrayAdapter;
    private ArrayList<Habit> habitArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed_user);

        getSupportActionBar().setTitle("Habit - User Habits");

        followed_user_uid = getIntent().getExtras().getString("User_Uid");

        UnsubBtn = findViewById(R.id.followed_user_unsubscribe_button);
        UserHabitsListView = findViewById(R.id.followed_user_habits_listview);
        UserNameTextView = findViewById(R.id.followed_user_name_textView);
        UserAgeTextView = findViewById(R.id.followed_user_age_textView);
        UserGenderTextView = findViewById(R.id.followed_user_gender_textView);
        UserAvatarImageView = findViewById(R.id.followed_user_avatar_imageView);

        // firebase connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // create the listView using the habit arrayList
        habitArrayList = new ArrayList<Habit>();
        habitArrayAdapter = new FollowedHabitListAdapter(this, habitArrayList);
        UserHabitsListView.setAdapter(habitArrayAdapter);


        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child(followed_user_uid).child("Info");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User = (Personal_info) snapshot.getValue(Personal_info.class);
                UserNameTextView.setText(User.getName());
                UserAgeTextView.setText("Age: "+User.getName());
                UserGenderTextView.setText("Gender: "+User.getGender());
                if (User.getDownloadUrl() != null){
                    Uri uri = Uri.parse(User.getDownloadUrl());
                    Glide.with(getApplicationContext()).load(uri).into(UserAvatarImageView);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // get all the habit the user has from the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(followed_user_uid).child("Habit");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habitArrayList.clear();
                // using the habits that retrieve from the database to set upt listView
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Habit hab = (Habit) dataSnapshot.getValue(Habit.class);
                    if (hab.isPublicHabit()){
                        habitArrayList.add(hab);
                    }
                }
                habitArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read Data Failed");
            }
        });

        UnsubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FollowedUserActivity.this);

                // Set a title for alert dialog
                builder.setTitle("Ready to Unsubscribe?");

                // Ask the final question
                builder.setMessage("Unsubscribe to this user mean you are no longer following this user. \n" +
                                    "You will not be able to view the habits of this corresponding user.");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                // User clicked the Yes button

                                // remove the uid in the followed by section of the followed user
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child(followed_user_uid).child("Follows").child("Followed_by");
                                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            if (dataSnapshot.getValue().equals(uid)){
                                                snapshot.getRef().removeValue();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        System.out.println("Read Data Failed");
                                    }
                                });

                                // remove the uid in the followed by section of the followed user
                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child(uid).child("Follows").child("Following");
                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            if (dataSnapshot.getValue().equals(followed_user_uid)){
                                                snapshot.getRef().removeValue();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        System.out.println("Read Data Failed");
                                    }
                                });



                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", dialogClickListener);

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No",dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });
    }

}
