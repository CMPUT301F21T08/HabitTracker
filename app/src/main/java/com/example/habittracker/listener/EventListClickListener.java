package com.example.habittracker.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.habittracker.HabitEvent;
import com.example.habittracker.HabitEventEditActivity;

public class EventListClickListener implements AdapterView.OnItemClickListener{
    ArrayAdapter<HabitEvent> habitEventAdapter;
    Context context;
    Activity activity;

    public EventListClickListener(Context context, Activity activity,ArrayAdapter<HabitEvent> habitEventAdapter){
        this.context = context;
        this.activity = activity;
        this.habitEventAdapter = habitEventAdapter;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        goToEventEditActivity(i);




    }
    /**
     * This method is used to shift to event edit activity from event list activity
     * @param index the index of pressed event in the event list
     */
    public void goToEventEditActivity(int index) {
        Intent intent = new Intent(context, HabitEventEditActivity.class);
        intent.putExtra("HabitEventForEdit", habitEventAdapter.getItem(index));
        intent.putExtra("EventIndex", index);
        activity.startActivity(intent);
    }

}
