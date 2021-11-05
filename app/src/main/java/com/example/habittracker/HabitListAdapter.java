/**
 * @author 'yhu19' and 'ingabire'
 *
 */
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The customized adapter for habit list
 */
public class HabitListAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habitArrayList;
    private Context context;

    private FirebaseAuth authentication;
    private String uid;

    /**
     * constructor of this HabitListAdapter
     */
    public HabitListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habitArrayList = habits;
        this.context = context;
    }

    /**
     * Process the view for each list element for habit event list
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
            view = LayoutInflater.from(context).inflate(R.layout.content_habit, parent, false);
        }

        Habit habit = habitArrayList.get(position);

        TextView habitTitleView = view.findViewById(R.id.allHabitsContent_habitContent_textView);
        habitTitleView.setText(habit.getHabitTitle());

        return view;
    }

}
