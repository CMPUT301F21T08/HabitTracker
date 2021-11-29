package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NotificationsActivity extends AppCompatActivity {


    // private variables
    private ListView request_users_ListView;
    private ArrayList<Personal_info> request_list;
    private ArrayAdapter<Personal_info> request_adapter;

    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // set up the listview for the activity
        request_users_ListView = findViewById(R.id.request_users_ListView);

        // firebase connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // set up the adapter with the requests list
        request_list = new ArrayList<Personal_info>();
        request_adapter = new NotificationListAdapter(this, request_list);
        // set up the listView with the adapter
        request_users_ListView.setAdapter(request_adapter);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Requests").child("Request_From");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                request_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String request_uid = (String) dataSnapshot.getValue();
                    // get the corresponding info with with uid and store it in the list
                    getInfoByUid(request_uid, new MyCallback_notification() {
                        @Override
                        public void onCallback(String request_uid, Personal_info user) {
                            request_list.add(user);
                            request_adapter.notifyDataSetChanged();
                            Toast.makeText(NotificationsActivity.this, user.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //Toast.makeText(NotificationsActivity.this, Integer.toString(request_list.size()), Toast.LENGTH_SHORT).show();
                request_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    public interface MyCallback_notification {
        void onCallback(String request_uid, Personal_info user);
    }

    /**
     * loop throught the firebase, and get the corresponding UID with given email
     */
    public void getInfoByUid(String request_uid, MyCallback_notification myCallback){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(request_uid).child("Info");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Personal_info info = (Personal_info) dataSnapshot.getValue(Personal_info.class);
                myCallback.onCallback(request_uid,info);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

    }

    /**
     * Process the KEY_RETURN signal in notification activity
     * When back button is pressed, return to following list activity
     */
    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(NotificationsActivity.this, FollowingActivity.class);
        startActivity(intent3);
        finish();
    }


}
