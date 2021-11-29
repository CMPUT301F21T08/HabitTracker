package com.example.habittracker;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FollowingListAdapter extends ArrayAdapter<Personal_info> {
    private ArrayList<Personal_info> FollowingArrayList;
    private Context context;
    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID


    /**
     * constructor of this FollowingListAdapter
     */
    public FollowingListAdapter(Context context, ArrayList<Personal_info> users) {
        super(context, 0, users);
        this.FollowingArrayList = users;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_following, parent, false);
        }
        // data base connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // get user in the requests list
        Personal_info User = FollowingArrayList.get(position);

        // set up the views in each row of listView
        TextView UserNameView = view.findViewById(R.id.Following_Content_User_textView);
        ImageView UserImage = view.findViewById(R.id.Following_Content_User_imageView);

        UserNameView.setText(User.getName());
        if (User.getDownloadUrl() != null){
            System.out.println(User.getDownloadUrl());
            Uri uri = Uri.parse(User.getDownloadUrl());
            Glide.with(context.getApplicationContext()).load(uri).into(UserImage);
        }


        return view;
    }
}
