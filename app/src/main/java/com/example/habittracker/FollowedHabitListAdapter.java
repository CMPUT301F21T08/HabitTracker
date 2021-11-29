package com.example.habittracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class FollowedHabitListAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habitArrayList;
    private Context context;
    private ProgressBar progression;

    private FirebaseAuth authentication;
    private String uid;

    public FollowedHabitListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habitArrayList = habits;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_followed_habit, parent, false);
        }

        Habit habit = habitArrayList.get(position);


        TextView habitTitleView = view.findViewById(R.id.allHabitsContent_habitContent_textView);
        habitTitleView.setText(habit.getHabitTitle());
        progression = view.findViewById(R.id.progression_bar);

        progression.getProgressDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        progression.setMax(100);
        if(habit.getNeedCompletion() == 0){
            progression.setProgress(0);
        } else {
            progression.setProgress((habit.getNumber_of_completion()*100)/habit.getNeedCompletion());
        }


        return view;
    }

}
