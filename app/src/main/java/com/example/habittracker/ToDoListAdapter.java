/**
 * @author 'yhu19'
 *
 */

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * The customized adapter for to do list
 */
public class ToDoListAdapter extends ArrayAdapter<Habit>{
    private ArrayList<Habit> habitArrayList;
    private Context context;
    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID

    /**
     * constructor of this HabitListAdapter
     */
    public ToDoListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habitArrayList = habits;
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
            view = LayoutInflater.from(context).inflate(R.layout.content_to_do, parent, false);
        }
        // data base connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // get the habit from the corresponding index in the listView
        Habit habit = habitArrayList.get(position);

        // set up the views in each row of listView
        TextView habitTitleView = view.findViewById(R.id.toDo_habitContent_textView);
        CheckBox done = view.findViewById(R.id.done_button);

        // get the index/position for which checkbox being checked
        done.setTag(position);
        CompoundButton.OnCheckedChangeListener ToDoListListener = new ToDoListCheckedChangedListener(getContext(), uid, done, habitArrayList);
        done.setOnCheckedChangeListener(ToDoListListener);
        // set the habit title in the to do list
        habitTitleView.setText(habit.getHabitTitle());
        // calculate the remaining time that the user need to do to finish the habit for today
        if(habit.getFrequencyType().equals("per day")){
            done.setText(String.valueOf(habit.getFrequency() - habit.getDoneTime()));
        } else {
            // for per week or per month habit, they only need to be done one time to finished
            done.setText(String.valueOf(1));
        }


        return view;
    }

}
