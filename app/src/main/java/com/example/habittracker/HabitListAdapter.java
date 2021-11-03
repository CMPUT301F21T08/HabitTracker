package com.example.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HabitListAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habitArrayList;
    private Context context;

    private FirebaseAuth authentication;
    private String uid;

    public HabitListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habitArrayList = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_habit, parent, false);
        }

        Habit habit = habitArrayList.get(position);

        TextView habitTitleView = view.findViewById(R.id.allHabitsContent_habitContent_textView);
        AppCompatImageButton deleteButton = view.findViewById(R.id.allHabits_delete_button);


        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        deleteButton.setTag(position);
        deleteButton.setOnClickListener(v -> {
            Integer index = (Integer) v.getTag();

            // remove the value in the firebase database
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").child(habitArrayList.get(index.intValue()).getHabitTitle());
            reference.removeValue();

            // remove in the ArrayList
            habitArrayList.remove(index.intValue());

            notifyDataSetChanged();
        });
        habitTitleView.setText(habit.getHabitTitle());

        return view;
    }

}

