package com.example.habittracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * The customized adapter for habit event list
 */
public class HabitEventListAdapter extends ArrayAdapter<HabitEvent> {
    private ArrayList<HabitEvent> habitEventArrayList;
    private Context context;

    public HabitEventListAdapter(Context context, ArrayList<HabitEvent> habitEvents) {
        super(context, 0, habitEvents);
        this.habitEventArrayList = habitEvents;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_habit_event, parent, false);
        }

        HabitEvent event = habitEventArrayList.get(position);

        TextView eventTitleView = view.findViewById(R.id.tv_habit_event);

        eventTitleView.setText(event.getEventTitle());

        return view;
    }

}
