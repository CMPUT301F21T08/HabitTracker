package com.example.habittracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class NotificationListAdapter extends ArrayAdapter<Personal_info> {

    private ArrayList<Personal_info> requestArrayList;
    private Context context;
    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID

    /**
     * constructor of this HabitListAdapter
     */
    public NotificationListAdapter(Context context, ArrayList<Personal_info> users) {
        super(context, 0, users);
        this.requestArrayList = users;
        this.context = context;
    }


    /**
     * Process the view for each list element for to do list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_notifications, parent, false);
        }
        // data base connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // get user in the requests list
        Personal_info User = requestArrayList.get(position);

        // set up the views in each row of listView
        TextView UserNameView = view.findViewById(R.id.Notification_User_textView);
        CheckBox Accept = view.findViewById(R.id.accept_button);
        CheckBox Decline = view.findViewById(R.id.decline_button);

        // get the index/position for which checkbox being checked
        Accept.setTag(position);
        Accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) {
                    // get the UserName that is being checked
                    Integer position = (Integer) buttonView.getTag();
                    Personal_info Accepted_user = requestArrayList.get(position);
                    String request_uid = Accepted_user.getUid();
                    String request_name = Accepted_user.getName();
                    String request_email = Accepted_user.getEmail();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Requests").child("Request_From");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // remove the uid in the requests_from branch
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if (snapshot.getValue().equals(request_uid)) {
                                    snapshot.getRef().removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.out.println("The read failed: " + error.getCode());
                        }
                    });

                    // put the accepted uid in the follower_by section in our firebase
                    HashMap<String,Object> map = new HashMap<>();
                    request_email = request_email.replace("@","");
                    request_email = request_email.replace(".","");
                    map.put(request_email,request_uid);
                    FirebaseDatabase.getInstance().getReference().child(uid).child("Follows").child("Followed_by").updateChildren(map);

                    // put our own uid into the requested uid's following branch, own email required
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child(uid).child("Info");
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String email = dataSnapshot.getValue(Personal_info.class).getEmail();
                            email = email.replace("@","");
                            email = email.replace(".","");
                            HashMap<String,Object> map1 = new HashMap<>();
                            map1.put(email,uid);
                            FirebaseDatabase.getInstance().getReference().child(request_uid).child("Follows").child("Following").updateChildren(map1);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });



                }
            }

        });

        // get the index/position for which checkbox being checked
        Decline.setTag(position);
        Decline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) {
                    // get the UserName that is being checked
                    Integer position = (Integer) buttonView.getTag();
                    Personal_info Accepted_user = requestArrayList.get(position);
                    String request_uid = Accepted_user.getUid();
                    String request_name = Accepted_user.getName();

                    System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" + request_name);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Requests").child("Request_From");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // remove the uid in the requests_from branch
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if (snapshot.getValue().equals(request_uid)) {
                                    snapshot.getRef().removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.out.println("The read failed: " + error.getCode());
                        }
                    });



                }
            }

        });
        // set the habit title in the to do list
        UserNameView.setText(User.getName());
        return view;
    }
}
