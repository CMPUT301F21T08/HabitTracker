package com.example.habittracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 'yhu19'
 * The customized adapter for habit list
 */
public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.HabitViewHolder> {
    // arraylist that used to store the habit
    private ArrayList<Habit> habitArrayList;
    // context of the activity
    private Context context;
    private View.OnClickListener onItemClickListener;
    private ProgressBar progression;
    private CheckBox disclose;




    /**
     * constructor of this HabitListAdapter
     *
     * @param context the context of the activity
     * @param habits the data set used to initialize the recycler view
     */
    public HabitListAdapter(Context context, ArrayList<Habit> habits) {
        this.habitArrayList = habits;
        this.context = context;
    }

    // Create new views
    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_habit, parent, false);
        return new HabitViewHolder(view);
    }

    // Replace the contents of a view using the data set
    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        // get the habit in the corresponding position
        Habit habit = habitArrayList.get(position);
        // using the title of habit to set up the textView
        holder.habitTitleView.setText(habit.getHabitTitle());
        // set up the disclosure status of the habit in the checkbox
        if(habit.isPublicHabit()){
            disclose.setChecked(true);
        }
        // attach the onCheckChangeListener to the checkbox to record the disclosure status of the habit
        disclose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                habit.setPublicHabit(isChecked);
            }
        });
        // set up the progress bar for the habit
        progression.getProgressDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN); // set color of the progression to be red
        progression.setMax(100);
        if(habit.getNeedCompletion() == 0){
            progression.setProgress(0);
        } else {
            // using the attributes, number_of_completion and needCompletion to set up the progression in the progress bar
            progression.setProgress((habit.getNumber_of_completion()*100)/habit.getNeedCompletion());
        }
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return habitArrayList.size();
    }

    /**
     * set up the onItemClickListener for the view
     * @param itemClickListener
     */
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    /**
     * Provide a reference to the views
     * (custom ViewHolder).
     */
    public class HabitViewHolder extends RecyclerView.ViewHolder {
        // variables of views
        public TextView habitTitleView;


        public HabitViewHolder(@NonNull View itemView) {
            // set up the views
            super(itemView);
            habitTitleView = itemView.findViewById(R.id.allHabitsContent_habitContent_textView);
            progression = itemView.findViewById(R.id.progression_bar);
            disclose = itemView.findViewById(R.id.disclose_checkBox);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }

}
