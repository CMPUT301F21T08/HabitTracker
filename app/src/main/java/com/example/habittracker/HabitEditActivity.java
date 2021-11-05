package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class HabitEditActivity extends AppCompatActivity implements AddWeekDaysFragment.OnFragmentInteractionListener {
    private Habit habit;
    private ArrayList<Integer> value_of_OccurrenceDate;
    // variable of views
    private Button addDate;
    private Button backBtn;
    private Button confirmBtn;
    private EditText title;
    private EditText content;
    private EditText reason;
    private EditText date;
    private EditText frequency;
    private TextView frequencyType;
    private Spinner spinner;
    private AlertDialog.Builder builder;


    private FirebaseAuth authentication;
    private String uid;

    // variables used to construct the spinner
    private ArrayList<String> frequencyList = new ArrayList<String>();
    private ArrayAdapter<String> spinnerAdapter;
    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                spinner.setSelection(0);
            }
            return false;
        }
    };
    // variable used to determine the action of this activity
    private String action;
    final Calendar myCalendar = Calendar.getInstance();
    // result code
    private int original= 44;
    private int newObject= 33;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_edit);
        builder = new AlertDialog.Builder(HabitEditActivity.this);
        addDate = findViewById(R.id.addDate_button);
        backBtn = findViewById(R.id.habitEdit_return_button);
        confirmBtn = findViewById(R.id.habitEdit_confirm_button);
        initArrayList();
        setView();
        spinner = findViewById(R.id.frequency_spinner);
        spinnerAdapter = new ArrayAdapter<String>(HabitEditActivity.this,android.R.layout.simple_spinner_item,frequencyList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnTouchListener(spinnerOnTouch);
        spinner.setSelection(0,false);
        action = getIntent().getStringExtra("action");
        if (action.equals("edit")){
            getSupportActionBar().setTitle("Habit - Edit");
            habit = (Habit) getIntent().getExtras().getSerializable("habit");
            initView(habit);
            frequencyType = findViewById(R.id.frequency_type);

        } else{
            getSupportActionBar().setTitle("Habit - Add");
        }
        authentication =FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }


        frequency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        });

        // set up a DatePickerDialog
        DatePickerDialog.OnDateSetListener time = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                addText();
            }
        };

        // this is to invoke a  DatePickerDialog when the EditText of date is tapped
        addDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(HabitEditActivity.this, time, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                frequency.setFocusableInTouchMode(false);
                frequency.setFocusable(false);
                setFragment(position);
                frequencyType.setText(frequencyList.get(position));
                ((TextView)view).setText(null);
                ((TextView)view).setTextSize(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getStringExtra("action").equals("add")){
                    Intent intentReturn = new Intent(HabitEditActivity.this, HabitListActivity.class);
                    startActivity(intentReturn);
                    finish();
                } else {
                    Intent intentReturn = new Intent();
                    action = "original";
                    intentReturn.putExtra("action", action);
                    setResult(original, intentReturn);
                    finish();
                }

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentConfirm = new Intent(getApplicationContext(), HabitListActivity.class);
                Bundle bundle = new Bundle();
                if(isValidInput()){
                    if(habit != null){
                        if(!habit.getHabitTitle().equals(title.getText().toString())){
                            // remove the value in the firebase database
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").child(habit.getHabitTitle());
                            reference.removeValue();
                            habit.setHabitTitle(title.getText().toString());
                        }
                        habit.setHabitTitle(title.getText().toString());
                        habit.setFrequency(Integer.parseInt(frequency.getText().toString()));
                        habit.setFrequencyType(frequencyType.getText().toString());
                        habit.setStartDate(date.getText().toString());
                        habit.setHabitContent(content.getText().toString());
                        habit.setHabitReason(reason.getText().toString());
                        habit.setOccurrenceDay(value_of_OccurrenceDate);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put(title.getText().toString(),habit);
                        FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                        bundle.putSerializable("habit", habit);
                        intentConfirm.putExtras(bundle);
                        setResult(newObject, intentConfirm);
                    } else {
                        String value_of_title = title.getText().toString();
                        String value_of_frequencyType = frequencyType.getText().toString();
                        int value_of_frequency = Integer.parseInt(frequency.getText().toString());
                        String value_of_startDate = date.getText().toString();
                        String value_of_content = content.getText().toString();
                        String value_of_reason = reason.getText().toString();
                        habit = new Habit(value_of_title, value_of_reason, value_of_content, value_of_startDate, value_of_frequency, value_of_frequencyType, value_of_OccurrenceDate);
                        // adding habit into the firebase
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(value_of_title,habit);
                        FirebaseDatabase.getInstance().getReference().child(uid).child("Habit").updateChildren(map);
                    }
                    startActivity(intentConfirm);
                    finish();
                } else {
                    AlertDialog alert;
                    alert = builder
                            .setTitle("Warning:")
                            .setMessage("Please enter all required information.")
                            .setNegativeButton("return", null).create();
                    alert.show();
                }
            }
        });
    }

    /* method that will create a correct format string that represent the date that selected in the
       datePickerDialog and add it to the EditText
     */
    private void addText() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        date.setText(sdf.format(myCalendar.getTime()));
    }

    // initialize the view
    private void setView() {
        title = findViewById(R.id.TitleInput);
        content = findViewById(R.id.contentInput);
        reason = findViewById(R.id.reasonInput);
        date = findViewById(R.id.dateInput);
        frequency = findViewById(R.id.frequencyInput);
        frequencyType = findViewById(R.id.frequency_type);

    }

    private void initView(Habit habit){
        title.setText(habit.getHabitTitle());
        date.setText(habit.getStartDate());
        frequency.setText(String.valueOf(habit.getFrequency()));
        content.setText(habit.getHabitContent());
        reason.setText(habit.getHabitReason());
        frequencyType.setText(habit.getFrequencyType());
        setView();
    }

    private void initArrayList(){
        frequencyList.add("choose frequency");
        frequencyList.add("per day");
        frequencyList.add("per week");
        frequencyList.add("per month");
    }

    private void setFragment(int position){
        switch(position){
            case 1:
                value_of_OccurrenceDate =  new ArrayList<Integer>();
                value_of_OccurrenceDate.add(-1);
                frequency.setFocusableInTouchMode(true);
                break;
            case 2:
                new AddWeekDaysFragment().show(getSupportFragmentManager(),"CHOOSE_OCCURRENCE_DATE");
                break;
            case 3:
                // to do later and send notification message
                AlertDialog alert;
                alert = builder
                        .setTitle("Message:")
                        .setMessage("Please choose another frequency, monthly frequency type is currently unsupported. Sorry!")
                        .setNegativeButton("return", null).create();
                alert.show();
                frequency.setText("");
                spinner.setSelection(0);
                break;
        }
    }

    public void onConfirmPressed(ArrayList<Integer> days, int valueOfFrequency){
        if(valueOfFrequency == 0){
            AlertDialog alert;
            alert = builder
                    .setTitle("Warning")
                    .setMessage("Frequency can not be zero, please choose again.")
                    .setNegativeButton("return", null).create();
            alert.show();
            spinner.setSelection(0);
        } else {
            value_of_OccurrenceDate = days;
            String text = "" + valueOfFrequency;
            frequency.setText(text);
            frequency = findViewById(R.id.frequencyInput);
        }
    }

    private boolean isValidInput(){
        boolean validInput = true;
        if(title.getText().toString().trim().length()==0){
            validInput = false;
        }
        if(reason.getText().toString().trim().length()==0){
            validInput = false;
        }
        if(content.getText().toString().trim().length()==0){
            validInput = false;
        }
        if(frequency.getText().toString().trim().length()==0){
            validInput = false;
        }
        if(date.getText().toString().trim().length()==0){
            validInput = false;
        }
        return validInput;
    }
}
