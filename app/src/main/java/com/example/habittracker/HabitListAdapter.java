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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class HabitListAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habitArrayList;
    private Context context;

    private FirebaseAuth authentication;
    private String uid;

    public HabitListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habitArrayList = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_habit, parent, false);
        }

        Habit habit = habitArrayList.get(position);

        TextView habitTitleView = view.findViewById(R.id.allHabitsContent_habitContent_textView);
        AppCompatImageButton deleteButton = view.findViewById(R.id.allHabits_delete_button);


        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        deleteButton.setTag(position);
        deleteButton.setOnClickListener(v -> {
            Integer index = (Integer) v.getTag();

            // Get the corresponding habit
            Habit habitToDelete = habitArrayList.get(index.intValue());
            removeAllRelatedEvent(habitToDelete.getEventList());


            // remove the value in the firebase database
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").child(habitToDelete.getHabitTitle());
            reference.removeValue();

            // remove in the ArrayList
            habitArrayList.remove(index.intValue());

            notifyDataSetChanged();
        });
        habitTitleView.setText(habit.getHabitTitle());

        return view;
    }

    /**
     * The function removes all habit event related to the current habit
     * @param eventList the list of habit event stored in the current habit
     */
    public void removeAllRelatedEvent(ArrayList<String> eventList) {
        for (int i = 0; i < eventList.size(); i++) {
            FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent").child(eventList.get(i)).removeValue();
            String pictureName = eventList.get(i)+".jpg";

            // Try to delete the picture
            try {
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/users/"+uid+"/"+pictureName);
                imageRef.delete();
            } catch(Exception e){
                System.out.println(e);
            }
        }
    }

}

