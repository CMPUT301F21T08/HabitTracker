/**
 * @author 'yhu19' and 'ingabire'
 * Allow user to see the detailed information of the habit
 *
 */

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

import com.example.habittracker.listener.HabitDescriptionEditListener;
import com.example.habittracker.listener.HabitDescriptionReturnListener;
import com.example.habittracker.listener.HabitDescriptionToEventListener;
import com.example.habittracker.listener.HabitListAddListener;
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

    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID

    // a activityResultLauncher used to send habit object to HabitEditActivity and receive the result habit from habitEditActivity
    private ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if the result code is newObject, that means user have modified the habit object, so we need to update the view
                    // in this page with the new habit object
                    if (result.getResultCode()== newObject){
                        // get the modified habit from HabitEditActivity
                        Intent data = result.getData();
                        Habit newHabit = (Habit) data.getExtras().getSerializable("habit");
                        // call this function to update the views of this page
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

        // get the habit object from HabitListActivity
        Bundle bundle = getIntent().getExtras();
        habit = (Habit) bundle.getSerializable("habit");
        position= bundle.getInt("position");

        // call this function to initial setup for the activity
        setView();
        // call this function to set up all information of habit in the page
        initView(habit);

        // initial setup for the buttons in this activity
        returnBtn = findViewById(R.id.description_return_button);
        deleteBtn = findViewById(R.id.description_delete_button);
        editBtn= findViewById(R.id.description_edit_button);
        toEventBtn = findViewById(R.id.description_habitEvent_button);

        // set up the onClickListener for the frequencyType button
        frequencyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if the frequencyType is "per week", then invoke a fragment to show the occurrence week day of the habit
                if(habit.getFrequencyType().equals("per week")){
                    new ShowWeekDaysFragment(habit).show(getSupportFragmentManager(),"SHOW_OCCURRENCE_DAYS");
                }
            }
        });

        // set up the onClickListener for the deleteBtn button
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // invoke a fragment to ask for confirmation for deletion of the habit from user and delete the habit if user click confirm
                new DeleteConfirmFragment("Are you sure you want to delete?").show(getSupportFragmentManager(),"DELETE_HABIT");
            }
        });

        // set up the onClickListener for the returnBtn button
        View.OnClickListener returnBtnOnclickListener = new HabitDescriptionReturnListener(getApplicationContext(), this);
        returnBtn.setOnClickListener(returnBtnOnclickListener);


        // set up the onClickListener for the editBtn button
        View.OnClickListener editBtnOnclickListener = new HabitDescriptionEditListener(getApplicationContext(), activityLauncher, habit);
        editBtn.setOnClickListener(editBtnOnclickListener);

        // set up the onClickListener for the toEventBtn button
        View.OnClickListener toEventBtnOnclickListener = new HabitDescriptionToEventListener(getApplicationContext(), this, habit);
        toEventBtn.setOnClickListener(toEventBtnOnclickListener);

        // firebase connection
        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

    }

    // a function that will get call when the user click the confirm button in the fragment.
    // this function will delete the current habit from the database and return to the HabitListActivity
    @Override
    public void onConfirmDeletePressed() {

        // remove the value in the firebase database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").child(habit.getHabitTitle());
        reference.removeValue();
        removeAllRelatedEvent(habit.getEventList());

        // return to the HabitListActivity
        Intent intentDelete= new Intent(getApplicationContext(), HabitListActivity.class);
        startActivity(intentDelete);
        finish();
    }

    //
    private void setView() {
        title = findViewById(R.id.description_habitTitle_textView);
        startDate = findViewById(R.id.description_startDate_textView);
        frequency = findViewById(R.id.description_frequency_textView);
        frequencyType = findViewById(R.id.description_frequencyType_button);
        content = findViewById(R.id.description_content_textView);
        reason = findViewById(R.id.description_reason_textView);
    }

    /**
     * The function will use the habit object to set up all information of habit to the page
     * @param habit
     */
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