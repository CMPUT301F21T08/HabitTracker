package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    private int add = 55;
    private int original= 44;
    private int newObject= 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_edit);
        addDate = findViewById(R.id.addDate_button);
        backBtn = findViewById(R.id.habitEdit_return_button);
        confirmBtn = findViewById(R.id.habitEdit_confirm_button);
        initArrayList();
        setView();
        action = getIntent().getStringExtra("action");
        if (action.equals("edit")){
            getSupportActionBar().setTitle("Habit - Edit");
            habit = (Habit) getIntent().getExtras().getSerializable("habit");
            initView(habit);
        } else{
            getSupportActionBar().setTitle("Habit - Add");
        }



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

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnTouchListener(spinnerOnTouch);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                frequency.setFocusableInTouchMode(false);
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
                Intent intentReturn = new Intent();
                action = "original";
                intentReturn.putExtra("action", action);
                setResult(original, intentReturn);
                finish();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentConfirm = new Intent();
                Bundle bundle = new Bundle();
                if(habit != null){
                    habit.setHabitTitle(title.getText().toString());
                    habit.setFrequencyType(frequencyType.getText().toString());
                    habit.setFrequency(Integer.parseInt(frequency.getText().toString()));
                    habit.setStartDate(date.getText().toString());
                    habit.setHabitContent(content.getText().toString());
                    habit.setHabitReason(reason.getText().toString());
                    habit.setOccurrenceDay(value_of_OccurrenceDate);
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
                    bundle.putSerializable("habit", habit);
                    intentConfirm.putExtras(bundle);
                    setResult(add, intentConfirm);
                }
                finish();
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
        spinner = findViewById(R.id.frequency_spinner);
        spinnerAdapter = new ArrayAdapter<String>(HabitEditActivity.this,android.R.layout.simple_spinner_item,frequencyList);
    }

    private void initView(Habit habit){
        title.setText(habit.getHabitTitle());
        date.setText(habit.getStartDate());
        frequency.setText(String.valueOf(habit.getFrequency()));
        frequencyType.setText(habit.getFrequencyType());
        content.setText(habit.getHabitContent());
        reason.setText(habit.getHabitReason());
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
            case 3:
                // to do later
                break;
        }
    }

    public void onConfirmPressed(ArrayList<Integer> days, int valueOfFrequency){
        value_of_OccurrenceDate = days;
        String text = "" + valueOfFrequency;
        frequency.setText(text);
        frequency = findViewById(R.id.frequencyInput);
    }
}