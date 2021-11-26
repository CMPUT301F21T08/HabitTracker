package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitDescriptionActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class HabitListClickListener implements View.OnClickListener{
    Context context;
    Activity activity;
    ArrayList<Habit> habitList;
    String uid;

    public HabitListClickListener(Activity activity,Context context, ArrayList<Habit> habitList, String uid ){
        this.context = context;
        this.activity = activity;
        this.habitList = habitList;
        this.uid = uid;

    }

    @Override
    public void onClick(View view) {
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
        int position = viewHolder.getAdapterPosition();
        Habit tapHabit = habitList.get(position);
        for(int i = 0; i < habitList.size(); i++){
            Habit habit = habitList.get(i);
            // upload the information to database to update all habit
            HashMap<String, Object> map = new HashMap<>();
            map.put(habit.getUUID(),habit);
            FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
        }
        goToHabitDescriptionActivity(position, tapHabit);
    }

    private void goToHabitDescriptionActivity(int position, Habit tapHabit){
        Intent intent = new Intent(context, HabitDescriptionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("habit",tapHabit);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.finish();
    }

}
