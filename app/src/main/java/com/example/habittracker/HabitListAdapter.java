/**
 * @author 'yhu19' and 'ingabire'
 *
 */
package com.example.habittracker;

import android.content.Context;
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
public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.HabitViewHolder> {
    private ArrayList<Habit> habitArrayList;
    private Context context;
    private FirebaseAuth authentication;
    private View.OnClickListener onItemClickListener;
    private String uid;
    private ProgressBar progression;




    /**
     * constructor of this HabitListAdapter
     */
    public HabitListAdapter(Context context, ArrayList<Habit> habits) {
        this.habitArrayList = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitArrayList.get(position);
        holder.habitTitleView.setText(habit.getHabitTitle());
        if(habit.isPublicHabit()){
            holder.disclose.setChecked(true);
        }
        holder.disclose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                habit.setPublicHabit(isChecked);
            }
        });

        progression.setMax(100);
        if(habit.getNeedCompletion() == 0){
            progression.setProgress(0);
        } else {
            progression.setProgress((habit.getDoneTime()*100)/habit.getNeedCompletion());
        }
    }

    @Override
    public int getItemCount() {
        return habitArrayList.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public class HabitViewHolder extends RecyclerView.ViewHolder {
        public TextView habitTitleView;
        public CheckBox disclose;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitTitleView = itemView.findViewById(R.id.allHabitsContent_habitContent_textView);
            progression = itemView.findViewById(R.id.progression_bar);
            disclose = itemView.findViewById(R.id.disclose_checkBox);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }

//    /**
//     * Process the view for each list element for habit event list
//     * @param position
//     * @param convertView
//     * @param parent
//     * @return
//     */
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = convertView;
//        if (view == null) {
//            view = LayoutInflater.from(context).inflate(R.layout.content_habit, parent, false);
//        }
//
//        Habit habit = habitArrayList.get(position);
//
//        TextView habitTitleView = view.findViewById(R.id.allHabitsContent_habitContent_textView);
//        habitTitleView.setText(habit.getHabitTitle());
//
//        return view;
//    }

}
