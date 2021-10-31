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

public class HabitDescriptionActivity extends AppCompatActivity implements DeleteConfirmFragment.OnDeleteConfirmFragmentInteractionListener {
    // views in this activity
    private Button returnBtn;
    private Button deleteBtn;
    private Button editBtn;
    private TextView title;
    private TextView startDate;
    private TextView frequency;
    private Button frequencyType;
    private TextView content;
    private TextView reason;
    // variables storing the value from last activity
    private Habit habit;
    private String action ="original";
    private int position;
    //result code
    private int original= 44;
    private int newObject= 33;
    private int delete = 22;
    private int back = 11;

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
                    if (result.getResultCode()==original){
                        Intent data = result.getData();
                        action = data.getStringExtra("action");
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

                Intent intentReturn = new Intent();
                Bundle bundle = new Bundle();
                intentReturn.putExtra("action", action);
                bundle.putSerializable("habit", habit);
                intentReturn.putExtras(bundle);
                setResult(back, intentReturn);
                finish();//return to the previous activity
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

    }

    @Override
    public void onConfirmDeletePressed() {
        Intent intentDelete= new Intent();
        String text = "" + position;
        intentDelete.putExtra("position", text);
        setResult(delete, intentDelete);
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
}