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

public class ToDoListAdapter extends ArrayAdapter<Habit>{
    private ArrayList<Habit> habitArrayList;
    private Context context;
    private FirebaseAuth authentication;
    private String uid;

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
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
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
                    Intent intent = new Intent(getContext(), HabitEventEditActivity.class);
                    Habit tappedHabit = habitArrayList.get(position);
                    if(tappedHabit.setDoneTime()){
                        String title = tappedHabit.getHabitTitle();
                        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

                        //Generate a unique id
                        String uniqueID = UUID.randomUUID().toString();

//                      tappedHabit.addEvent(title+": "+ date);
                        tappedHabit.addEvent(uniqueID);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put(title,tappedHabit);
                        FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);

                        intent.putExtra("EventIndex", -1);
                        intent.putExtra("HabitName", title);
                        intent.putExtra("UniqueID", uniqueID);

                        context.startActivity(intent);
                        ((MainPageActivity)context).finish();
                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(tappedHabit.getHabitTitle(),tappedHabit);
                        FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                        done.setChecked(false);
                    }

                }
            }

        });
        habitTitleView.setText(habit.getHabitTitle());
        if(habit.getFrequencyType().equals("per day")){
            done.setText(String.valueOf(habit.getFrequency() - habit.getDoneTime()));
        } else {
            done.setText(String.valueOf(1));
        }


        return view;
    }

}
