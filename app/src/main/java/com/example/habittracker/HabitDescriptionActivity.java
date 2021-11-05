package com.example.habittracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HabitDescriptionActivity extends AppCompatActivity implements DeleteConfirmFragment.OnDeleteConfirmFragmentInteractionListener {
    // views in this activity
    private Button returnBtn;
    private Button deleteBtn;
    private Button editBtn;
    private TextView title;
    private TextView startDate;
    private TextView frequency;
    private Button frequencyType;
    private Button toEventBtn;
    private TextView content;
    private TextView reason;
    // variables storing the value from last activity
    private Habit habit;
    private String action ="original";
    private int position;
    //result code
    private int newObject= 33;

    private FirebaseAuth authentication;
    private String uid;


    private ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()== newObject){
                        Intent data = result.getData();
                        Habit newHabit = (Habit) data.getExtras().getSerializable("habit");
                        initView(newHabit);
                        habit = newHabit;
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_description);

        getSupportActionBar().setTitle("Habit - Description");


        Bundle bundle = getIntent().getExtras();


        habit = (Habit) bundle.getSerializable("habit");
        position= bundle.getInt("position");
        setView();
        initView(habit);

        returnBtn = findViewById(R.id.description_return_button);
        deleteBtn = findViewById(R.id.description_delete_button);
        editBtn= findViewById(R.id.description_edit_button);
        toEventBtn = findViewById(R.id.description_habitEvent_button);


        frequencyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(habit.getFrequencyType().equals("per week")){
                    new ShowWeekDaysFragment(habit).show(getSupportFragmentManager(),"SHOW_OCCURRENCE_DAYS");
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteConfirmFragment("Are you sure you want to delete?").show(getSupportFragmentManager(),"DELETE_HABIT");
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentReturn = new Intent(HabitDescriptionActivity.this, HabitListActivity.class);
                startActivity(intentReturn);
                finish();//return to the HabitListActivity
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = "new";
                Intent intentEdit = new Intent(getApplicationContext(), HabitEditActivity.class);
                intentEdit.putExtra("action", "edit");
                Bundle bundle= new Bundle();
                bundle.putSerializable("habit", habit);
                intentEdit.putExtras(bundle);
                activityLauncher.launch(intentEdit);
            }
        });

        toEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentEvent = new Intent(HabitDescriptionActivity.this, HabitEventsOfHabitActivity.class);
                Bundle bundleEvent = new Bundle();
                bundleEvent.putSerializable("habit", habit);
                intentEvent.putExtras(bundleEvent);
                startActivity(intentEvent);
                finish();//return to the HabitListActivity
            }
        });

        // firebase connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

    }

    // not supported right now with database
    @Override
    public void onConfirmDeletePressed() {

        // remove the value in the firebase database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").child(habit.getHabitTitle());
        reference.removeValue();
        removeAllRelatedEvent(habit.getEventList());

        Intent intentDelete= new Intent(getApplicationContext(), HabitListActivity.class);
        startActivity(intentDelete);
        finish();
    }

    // link the backend to frontend
    private void setView() {
        title = findViewById(R.id.description_habitTitle_textView);
        startDate = findViewById(R.id.description_startDate_textView);
        frequency = findViewById(R.id.description_frequency_textView);
        frequencyType = findViewById(R.id.description_frequencyType_button);
        content = findViewById(R.id.description_content_textView);
        reason = findViewById(R.id.description_reason_textView);
    }

    // initialize the view
    private void initView(Habit habit ){
        title.setText(habit.getHabitTitle());
        startDate.setText(habit.getStartDate());
        String type = "times " + habit.getFrequencyType();
        frequency.setText(String.valueOf(habit.getFrequency()));
        frequencyType.setText(type);
        content.setText(habit.getHabitContent());
        reason.setText(habit.getHabitReason());
        setView();
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