package com.example.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ToDoListAdapter extends ArrayAdapter<Habit>{
    private ArrayList<Habit> habitArrayList;
    private Context context;

    public ToDoListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habitArrayList = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_to_do, parent, false);
        }

        Habit habit = habitArrayList.get(position);

        TextView habitTitleView = view.findViewById(R.id.toDo_habitContent_textView);
        CheckBox done = view.findViewById(R.id.done_button);

        done.setTag(position);
        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) {
                    Integer position = (Integer) buttonView.getTag();

                }
            }

        });
        habitTitleView.setText(habit.getHabitTitle());

        return view;
    }

}
