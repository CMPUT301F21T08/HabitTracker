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

import java.util.ArrayList;

public class HabitListAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habitArrayList;
    private Context context;

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

        deleteButton.setTag(position);
        deleteButton.setOnClickListener(v -> {
            Integer index = (Integer) v.getTag();
            habitArrayList.remove(index.intValue());
            notifyDataSetChanged();
        });
        habitTitleView.setText(habit.getHabitTitle());

        return view;
    }

}

