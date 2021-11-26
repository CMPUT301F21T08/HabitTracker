/**
 * @author 'yhu19' and 'ingabire'
 * Allow users to see the list of habits they have
 *
 */

package com.example.habittracker;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import com.example.habittracker.listener.NavigationBarClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.habittracker.listener.EventEditConfirmListener;
import com.example.habittracker.listener.HabitListAddListener;
import com.example.habittracker.listener.NavigationBarClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class HabitListActivity extends AppCompatActivity {
    // views of the activity
    private RecyclerView habitRecycleView;
    private FloatingActionButton addButton;
    private HabitListAdapter habitAdapter;
    private ArrayList<Habit> habitList;
    private int amount;

    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
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
    };



    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);
        getSupportActionBar().setTitle("Habit - All Habits");
        // set up the view for the activity
        habitRecycleView = findViewById(R.id.allHabits_habitList_recycleView);
        addButton = findViewById(R.id.allHabits_addButton_button);

        // firebase connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // create the listView using the habit arrayList
        habitList = new ArrayList<Habit>();
        habitAdapter = new HabitListAdapter(this, habitList);
        habitRecycleView.setAdapter(habitAdapter);
        habitRecycleView.setLayoutManager(new LinearLayoutManager(this));
        habitAdapter.setOnItemClickListener(onItemClickListener);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(habitList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                habitAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                for(int i = 0; i < habitList.size(); i++){
                    Habit habit = habitList.get(i);
                    habit.setIndex(i);
                }
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // no-op
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        });
        touchHelper.attachToRecyclerView(habitRecycleView);



        // get all the habit the user has from the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habitList.clear();
                // using the habits that retrieve from the database to set upt listView
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Habit hab = (Habit) dataSnapshot.getValue(Habit.class);
                    habitList.add(hab);
                }
                for (int i = 0; i < habitList.size() - 1; i++){
                    if (habitList.get(i).getIndex() > habitList.get(i+1).getIndex()) {

                        // Swapping the elements.
                        Habit temp = habitList.get(i);
                        habitList.set(i, habitList.get(i+1));
                        habitList.set(i+1, temp);
                        i = -1;
                    }
                }
                amount = habitList.size();
                habitAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read Data Failed");
            }
        });



//        // set up the OnItemClickListener to allow the user to click on the habit to see its detailed information
//        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Habit tapHabit = habitList.get(i);
//                goToHabitDescriptionActivity(i, tapHabit);
//
//            }
//        });

        // set up the OnClickListener to allow user to add a new habit
        //View.OnClickListener addBtnOnclickListener = new HabitListAddListener(getApplicationContext(), this, amount);
        //addButton.setOnClickListener(addBtnOnclickListener);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < habitList.size(); i++){
                    Habit habit = habitList.get(i);
                    // upload the information to database to update all habit
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(habit.getUUID(),habit);
                    FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                }
                Intent intent = new Intent(getApplicationContext(), HabitEditActivity.class);
                intent.putExtra("amount", amount);
                intent.putExtra("action", "add");
                startActivity(intent);
                finish();
            }
        });





        // Process Navigation Bar
        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_habit);

        //NavigationBarView.OnItemSelectedListener bottomNavigationViewOnItemSelectedListener = new NavigationBarClickListener(getApplicationContext(),this);
        //bottomNavigationView.setOnItemSelectedListener(bottomNavigationViewOnItemSelectedListener);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for(int i = 0; i < habitList.size(); i++){
                    Habit habit = habitList.get(i);
                    // upload the information to database to update all habit
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(habit.getUUID(),habit);
                    FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                }
                switch (item.getItemId()) {
                    case R.id.navigation_habit:
                        return true;
                    case R.id.navigation_habitEvent:
                        Intent intent1 = new Intent(HabitListActivity.this, HabitEventListActivity.class);
                        intent1.putExtra("StartMode", "normal");
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.navigation_homePage:
                        Intent intent2 = new Intent(HabitListActivity.this, MainPageActivity.class);
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.navigation_following:
                        Intent intent3 = new Intent(HabitListActivity.this, FollowingActivity.class);
                        startActivity(intent3);
                        finish();
                        return true;
                    case R.id.navigation_settings:
                        Intent intent4 = new Intent(HabitListActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }


    // a function used to go to the habit description page with the tapped habit
    private void goToHabitDescriptionActivity(int position, Habit tapHabit){
        Intent intent = new Intent(this, HabitDescriptionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("habit",tapHabit);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
