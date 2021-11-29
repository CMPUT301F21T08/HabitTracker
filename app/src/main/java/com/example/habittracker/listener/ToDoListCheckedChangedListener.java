package com.example.habittracker.listener;

import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEventEditActivity;
import com.example.habittracker.MainPageActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ToDoListCheckedChangedListener implements CompoundButton.OnCheckedChangeListener{
    //attributes
    ArrayList<Habit> habitArrayList;
    Context context;

    String uid;
    CheckBox done;
    public ToDoListCheckedChangedListener(Context context, String uid, CheckBox done, ArrayList<Habit> habitArrayList){
        this.context = context;

        this.habitArrayList= habitArrayList;
        this.done= done;
        this.uid= uid;

    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Integer position = (Integer) buttonView.getTag();
            Intent intent = new Intent(context, HabitEventEditActivity.class);
            Habit tappedHabit = habitArrayList.get(position);
            if (tappedHabit.setDoneTime()) {
                String title = tappedHabit.getHabitTitle();
                String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

                //Generate a unique id for habit event
                String uniqueID = UUID.randomUUID().toString();

                tappedHabit.addEvent(uniqueID);

                HashMap<String, Object> map = new HashMap<>();
                map.put(tappedHabit.getUUID(), tappedHabit);
                FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);

                intent.putExtra("EventIndex", -1);
                intent.putExtra("HabitName", title);
                intent.putExtra("UniqueID", uniqueID);

                context.startActivity(intent);
                ((MainPageActivity) context).finish();
            } else {
                HashMap<String, Object> map = new HashMap<>();
                map.put(tappedHabit.getHabitTitle(), tappedHabit);
                FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                done.setChecked(false);
            }

        }
    }
}
