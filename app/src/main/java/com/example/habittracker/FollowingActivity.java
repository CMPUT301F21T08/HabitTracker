package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FollowingActivity extends AppCompatActivity implements SearchFollowingFragment.OnFragmentInteractionListener{

    BottomNavigationView bottomNavigationView;


    private ListView following_ListView;
    private ArrayList<Personal_info> following_list;
    private ArrayAdapter<Personal_info> following_adapter;


    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID
    private String email;


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.top_action_bar_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_following);

        following_ListView = findViewById(R.id.following_listview);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.showOverflowMenu();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Following");

        // firebase connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }


        // ListView for the following list
        following_list = new ArrayList<Personal_info>();

        following_adapter = new FollowingListAdapter(this,following_list);
        following_ListView.setAdapter(following_adapter);


        // fetch the followed list in the firebase to show
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Follows").child("Following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following_list.clear();
                ArrayList<String> uids_following = new ArrayList<String>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    // get the corresponding uid
                    String uid_following = (String) snapshot1.getValue();
                    System.out.println("uid following: "+uid_following);
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child(uid_following).child("Info");
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Personal_info info = (Personal_info) snapshot.getValue(Personal_info.class);

                            //****later change the store the entire info class in the list, create a new adatper file to manage changes
                            following_list.add(info);
                            following_adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        // set up the OnItemClickListener to allow the user to click on the Users in the following list
        following_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Personal_info taped_user = following_list.get(i);
                Intent intent = new Intent(FollowingActivity.this, FollowedUserActivity.class);
                intent.putExtra("User_Uid",taped_user.getUid());
                startActivity(intent);

            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_habit:
                        Intent intent1 = new Intent(FollowingActivity.this, HabitListActivity.class);
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.navigation_habitEvent:
                        Intent intent2 = new Intent(FollowingActivity.this, HabitEventListActivity.class);
                        intent2.putExtra("StartMode", "normal");
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.navigation_homePage:
                        Intent intent3 = new Intent(FollowingActivity.this, MainPageActivity.class);
                        startActivity(intent3);
                        finish();
                        return true;
                    case R.id.navigation_following:
                        return true;
                    case R.id.navigation_settings:
                        Intent intent4 = new Intent(FollowingActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Process the KEY_RETURN signal in following activity
     * When back button is pressed, return to main page activity
     */
    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(FollowingActivity.this, MainPageActivity.class);
        startActivity(intent3);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                new SearchFollowingFragment().show(getSupportFragmentManager(),"Search");
                return true;

            case R.id.notification:
                Intent intent = new Intent(FollowingActivity.this, NotificationsActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public interface MyCallback {
        void onCallback(String email_toFollow, String uid_toFollow);
    }

    public void getUidByEmail(String email_toFollow, MyCallback myCallback){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // loop the entire firebase to find the corresponding uid with the email
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Personal_info info = (Personal_info) snapshot.child("Info").getValue(Personal_info.class);
                    if (info.getEmail().equals(email_toFollow)) {
                        myCallback.onCallback(email_toFollow, snapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

    }



    @Override
    public void onConfirmPressed(String email_toFollow) {
        authentication = FirebaseAuth.getInstance();
        Toast.makeText(FollowingActivity.this, "Request sent to "+ email_toFollow + " !", Toast.LENGTH_SHORT).show();
        getUidByEmail(email_toFollow, new MyCallback() {
            // as onDataChange was called asynchronous, we created a onCallback method to store the uid_toFollow
            @Override
            public void onCallback(String email_toFollow, String uid_toFollow) {
                // put the uid into the Request_To list
                HashMap<String,Object> map = new HashMap<>();
                email_toFollow = email_toFollow.replace("@","");
                email_toFollow = email_toFollow.replace(".","");
                map.put(email_toFollow,uid_toFollow);
                FirebaseDatabase.getInstance().getReference().child(uid).child("Requests").child("Request_To").updateChildren(map);

                // put our own uid into the requested uid's request from branch, own email required
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Info");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         String email = dataSnapshot.getValue(Personal_info.class).getEmail();
                         email = email.replace("@","");
                         email = email.replace(".","");
                         HashMap<String,Object> map1 = new HashMap<>();
                         map1.put(email,uid);
                         FirebaseDatabase.getInstance().getReference().child(uid_toFollow).child("Requests").child("Request_From").updateChildren(map1);
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) { }
                 });
            }
        });
    }


    @Override
    public void errorMessage(int i) {
        if (i == 0){
            Toast.makeText(FollowingActivity.this, "Invalid Input for Email!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(FollowingActivity.this, "User does not exist with this email!", Toast.LENGTH_SHORT).show();
        }
    }


}
