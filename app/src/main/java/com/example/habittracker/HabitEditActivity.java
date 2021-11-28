/**
 * @author 'yhu19'
 * Allow user to modefied the information of the existing habit or add a new habit
 *
 */

package com.example.habittracker;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habittracker.listener.HabitEditBackListener;
import com.example.habittracker.listener.HabitEditConfirmListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity will allow user to edit a habit in the habit list/add a habit to the habit list
 */
public class HabitEditActivity extends AppCompatActivity implements AddWeekDaysFragment.OnFragmentInteractionListener {
    // public variable used to send the occurrence day of habit to HabitEditConfirmListener
    public static ArrayList<Integer> value_of_OccurrenceDate;
    // public variable used to send the disclosure status of habit to HabitEditConfirmListener
    public static boolean publicHabit;
    // variable that storing the Habit object
    private Habit habit;
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
    private CheckBox disclose;
    // variable used to set up a AlertDialog
    private AlertDialog.Builder builder;

    private FirebaseAuth authentication; // user authentication reference
    private String uid; // User unique ID
    private String message;

    // variables used to construct the spinner
    private ArrayList<String> frequencyList = new ArrayList<String>();
    private ArrayAdapter<String> spinnerAdapter;
    // set up the OnTouchListener for spinner
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
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_edit);
        // initialize the set up of the activity
        builder = new AlertDialog.Builder(HabitEditActivity.this);
        addDate = findViewById(R.id.addDate_button);
        backBtn = findViewById(R.id.habitEdit_return_button);
        confirmBtn = findViewById(R.id.habitEdit_confirm_button);
        // initialize the arraylist that will be used to set up the spinner content
        initArrayList();
        // call the function to set up some views for the activity
        setView();
        // set up the spinner
        spinner = findViewById(R.id.frequency_spinner);
        spinnerAdapter = new ArrayAdapter<String>(HabitEditActivity.this,android.R.layout.simple_spinner_item,frequencyList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnTouchListener(spinnerOnTouch);
        spinner.setSelection(0,false);
        // get the action string from last activity to determine this activity is used for editing or adding a habit
        action = getIntent().getStringExtra("action");
        // input the information of the habit being edited to the page
        if (action.equals("edit")){
            getSupportActionBar().setTitle("Habit - Edit");
            habit = (Habit) getIntent().getExtras().getSerializable("habit");
            index = habit.getIndex();
            // input the information of habit to the view in the activity
            initView(habit);
            frequencyType = findViewById(R.id.frequency_type);
            value_of_OccurrenceDate = habit.getOccurrenceDay();
        } else{
            getSupportActionBar().setTitle("Habit - Add");
            index = getIntent().getIntExtra("amount", 0);
        }

        // firebase connection
        authentication =FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }

        // set up the onCheckedChangeListener to record the disclosure status of the habit
        disclose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                publicHabit = isChecked;
            }
        });

        // this is a TextWatcher used to avoid user entering 0 or number that start with 0 in the EditText
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

        // this is to invoke a DatePickerDialog when the EditText of date is tapped
        addDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                date.setVisibility(View.VISIBLE);
                new DatePickerDialog(HabitEditActivity.this, time, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // set up a OnItemSelectedListener for spinner to record the choice in the spinner that is chosen by the user
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // set up the editText for frequency to disallow user to input data
                frequency.setVisibility(View.VISIBLE);
                frequency.setFocusableInTouchMode(false);
                frequency.setFocusable(false);
                // call this function to guide user input required information
                setFragment(position);
                // show what the choice user has chosen in the textView for frequencyType
                frequencyType.setText(frequencyList.get(position));
                // let the text of the spinner disappear to avoid duplicate information in the screen
                ((TextView)view).setText(null);
                ((TextView)view).setTextSize(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // set up the backBtn to allow user to go back to the previous activity
        message = getIntent().getStringExtra("action");
        View.OnClickListener backBtnOnclickListener = new HabitEditBackListener(getApplicationContext(), this, action, original, message);
        backBtn.setOnClickListener(backBtnOnclickListener);

        // set up the backBtn to upload the habit to the database
        View.OnClickListener confirmBtnOnclickListener;
        confirmBtnOnclickListener = new HabitEditConfirmListener(getApplicationContext(), this, title, content, reason, date, frequency, frequencyType, habit, authentication, uid, newObject, index);
        confirmBtn.setOnClickListener(confirmBtnOnclickListener);
    }

    /**
     * Process the KEY_RETURN signal in the habit edit activity
     * When back button is pressed, return to the page where the user is at previously
     * Either description activity or list activity
     */
    @Override
    public void onBackPressed() {
        Intent intentReturn;
        if(message.equals("add")){
            // go back to HabitListActivity
            intentReturn = new Intent(this, HabitListActivity.class);
            startActivity(intentReturn);
        } else {
            // go back to HabitDescriptionActivity with the modified habit
            intentReturn = new Intent();
            action = "original";
            intentReturn.putExtra("action", action);
            setResult(original, intentReturn);
        }
        finish();
    }

    /**
     * method that will create a correct format string that represent the date that selected in the
     * datePickerDialog and add it to the EditText
     */
    private void addText() {
        // the format of the date that we will store for the habit
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        date.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * method that sets up the view for the activity
     */
    private void setView() {
        title = findViewById(R.id.TitleInput);
        content = findViewById(R.id.contentInput);
        reason = findViewById(R.id.reasonInput);
        date = findViewById(R.id.dateInput);
        frequency = findViewById(R.id.frequencyInput);
        frequencyType = findViewById(R.id.frequency_type);
        disclose = findViewById(R.id.disclose_checkBox_edit);
    }

    /**
     * method that inputs all information of the existing habit in the corresponding view
     */
    private void initView(Habit habit){
        title.setText(habit.getHabitTitle());
        date.setText(habit.getStartDate());
        frequency.setText(String.valueOf(habit.getFrequency()));
        content.setText(habit.getHabitContent());
        reason.setText(habit.getHabitReason());
        frequencyType.setText(habit.getFrequencyType());
        if(habit.isPublicHabit()){
            disclose.setChecked(true);
        }
        setView();
    }

    /**
     * method that initializes the arrayList for the spinner
     */
    private void initArrayList(){
        frequencyList.add("choose frequency");
        frequencyList.add("per day");
        frequencyList.add("per week");
        frequencyList.add("per month");
    }

    /**
     * method that sets up the relevant view for the user to input frequency when they choose frequency type
     */
    private void setFragment(int position){
        switch(position){
            case 1:
                // add -1 to the occurrence day arrayList if the user choose per day frequency
                value_of_OccurrenceDate =  new ArrayList<Integer>();
                value_of_OccurrenceDate.add(-1);
                // set up the frequency editText to allow user input frequency
                frequency.setFocusableInTouchMode(true);
                break;
            case 2:
                // call this fragment to allow user choose occurrence days if they choose per week frequency
                new AddWeekDaysFragment().show(getSupportFragmentManager(),"CHOOSE_OCCURRENCE_DATE");
                break;
            case 3:
                // call the DateDialog to set up the monthly occurrence day
                DateDialog();
                break;
        }
    }

    /**
     * method that will be called when the user click the confirm button in the AddWeekDaysFragment
     * @param valueOfFrequency the amount of weekly occurrence days the user chose for the habit
     * @param days  the arraylist that stores all the weekly occurrence days the user chose
     */
    public void onConfirmPressed(ArrayList<Integer> days, int valueOfFrequency){
        // invoke the alert dialog if the user choose nothing to avoid 0 frequency appears
        if(valueOfFrequency == 0){
            AlertDialog alert;
            alert = builder
                    .setTitle("Warning")
                    .setMessage("Frequency can not be zero, please choose again.")
                    .setNegativeButton("return", null).create();
            alert.show();
            spinner.setSelection(0);
        } else {
            // add the corresponding week day in the habit attribute
            value_of_OccurrenceDate = days;
            String text = "" + valueOfFrequency;
            frequency.setText(text);
            frequency = findViewById(R.id.frequencyInput);
        }
    }

    /**
     * method that sets up a date picker dialog to allow user choose the monthly occurrence day and record it
     */
    public void DateDialog(){
        // set up the date picker dialog
        Calendar cal;
        int day;
        int month;
        int year;
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {
                // store the monthly occurrence day in the habit object
                value_of_OccurrenceDate = new ArrayList<Integer>();
                value_of_OccurrenceDate.add(dayOfMonth);
                // set up the view to display the frequency
                frequency.setText("1");
                frequency = findViewById(R.id.frequencyInput);
            }};

        // show the date picker dialog
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();

    }
}
