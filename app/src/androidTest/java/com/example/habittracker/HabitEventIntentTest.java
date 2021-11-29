package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import java.util.ArrayList;


// We created user with info userName 'Test', email 'test@gmail.com', password '123456'
/**
 * Test class for Habit Event List Activity
 */
@RunWith(AndroidJUnit4.class)
public class HabitEventIntentTest {
    private Solo solo;

    private HabitEventListAdapter testAdapter;
    private ArrayList<HabitEvent> testList;
    private HabitEvent testEvent;


    @Rule
    public ActivityTestRule<LogInActivity> rule =
            new ActivityTestRule<>(LogInActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        testList = new ArrayList<>();

        // Create mock habit event
        testEvent = new HabitEvent("Run", "11111", "Edmonton", "1111-2222", "3333-4444");
        testList.add(testEvent);
        testAdapter = new HabitEventListAdapter(ApplicationProvider.getApplicationContext(), testList);
    }

    /**
     * Gets the Activity
     * @throws Exception
     */

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();

    }

    /**
     * Check if we can add new habit event
     */
    @Test
    public void checkAddNewHabitEvent() {
        //Asserts that current activity is the LogInActivity
        // because that is where we start
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "123456nnn@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456nnn");
        solo.clickOnButton("Login");
        //After logIn we go to the Main Page
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        //From Main Page, we need to click on Habits events
        // So that we can see all habit events


        solo.clickOnView(solo.getView(R.id.navigation_habitEvent));
        solo.clickOnText("Habit");

       HabitEditActivity habitEditActivity = (HabitEditActivity) solo.getCurrentActivity();
       EditText titleText = habitEditActivity.findViewById(R.id.TitleInput);

//        solo.enterText(titleText,"habitTest1");
//
//        solo.clickOnText("Start Date");
//
//        solo.setDatePicker(0, 2021, 11, 1);
//        solo.clickOnText("OK");
//
//
//        solo.clickOnView(solo.getView(R.id.frequency_spinner));
//        solo.clickOnText("per day");


        solo.enterText((EditText) solo.getView(R.id.TitleInput), "Habit1");//habit title
        solo.enterText((EditText) solo.getView(R.id.dateInput), "2021-11-06");// startDate
        solo.pressSpinnerItem(0, 1);// for daily
        solo.enterText((EditText) solo.getView(R.id.frequencyInput), "2");//times per day
        solo.enterText((EditText) solo.getView(R.id.contentInput), "This is a test");
        solo.enterText((EditText) solo.getView(R.id.reasonInput), "Started for testing");
        solo.clickOnButton("Confirm");


    }

    /**
     * check if we can edit a habit event and confirm to the habit event page
     */
    @Test
    public void checkConfirm() {
        //Asserts that current activity is the LogInActivity
        // because that is where we start
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "123456nnn@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456nnn");
        solo.clickOnButton("Login");
        //After logIn we go to the Main Page
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        //From Main Page, we need to click on Habits events
        // So that we can see all habit events


        solo.clickOnView(solo.getView(R.id.navigation_habitEvent));
        solo.clickOnText("Habit Event");


        //Asserts that current activity is the HabitEventListActivity
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

        // get the first habit event
        // only habit in the one
        //assert in correct Activity
        HabitEventListActivity activity = (HabitEventListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.lv_habit_event);
        HabitEvent newHabE = (HabitEvent) listView.getItemAtPosition(0);
        String eventName = newHabE.getEventTitle();


        solo.clickOnText(eventName);


        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);


        // click on upload photo button

        // click on camera button
        // click on get location button

        solo.clickOnText("get location");
        solo.assertCurrentActivity("Wrong Activity", HabitEventEditMapActivity.class);


        solo.clickOnText("return");
        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);

        // click on confirm button
        solo.clickOnText("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);



        HabitEventListActivity activity2 = (HabitEventListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView2 = activity2.findViewById(R.id.lv_habit_event);
        HabitEvent newHabE2 = (HabitEvent) listView2.getItemAtPosition(0);
        String eventName2 = newHabE2.getEventTitle();
        assertEquals(eventName2,eventName);




    }



    /**
     * check if we can delete a habit event to the habit event page
     */
    @Test
    public void checkDelete() {
        //Asserts that current activity is the LogInActivity
        // because that is where we start
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "123456nnn@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456nnn");
        solo.clickOnButton("Login");
        //After logIn we go to the Main Page
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        //From Main Page, we need to click on Habits events
        // So that we can see all habit events


        solo.clickOnView(solo.getView(R.id.navigation_habitEvent));
        solo.clickOnText("Habit Event");


        //Asserts that current activity is the HabitEventListActivity
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

        // get the first habit event
        // only habit in the one
        //assert in correct Activity
        HabitEventListActivity activity = (HabitEventListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.lv_habit_event);
        HabitEvent newHabE = (HabitEvent) listView.getItemAtPosition(0);
        String eventName = newHabE.getEventTitle();


        solo.clickOnText(eventName);


        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);


        // click on upload photo button

        // click on camera button
        // click on get location button

        solo.clickOnText("get location");
        solo.assertCurrentActivity("Wrong Activity", HabitEventEditMapActivity.class);


        solo.clickOnText("return");
        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);


        // click on confirm button
        solo.clickOnText("Delete Event");
        solo.clickOnText("Confirm");
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

        // click on delete event button





    }

    /**
     * Test whether clicking the camera button in the habit event edit page actually opens a camera interface
     */
    @Test
    public void checkCamera(){
        //Asserts that current activity is the LogInActivity
        // because that is where we start
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "123456nnn@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456nnn");
        solo.clickOnButton("Login");
        //After logIn we go to the Main Page
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        //From Main Page, we need to click on Habits events
        // So that we can see all habit events

        solo.clickOnView(solo.getView(R.id.navigation_habitEvent));
        solo.clickOnText("Habit Event");


        //Asserts that current activity is the HabitEventListActivity
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

        // get the first habit event
        // only habit in the one
        //assert in correct Activity
        HabitEventListActivity activity = (HabitEventListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.lv_habit_event);
        HabitEvent newHabE = (HabitEvent) listView.getItemAtPosition(0);
        String eventName = newHabE.getEventTitle();


        solo.clickOnText(eventName);

        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);

        solo.clickOnText("Camera");
    }


    /**
     * Test whether we can successfully get the location information by pressing the get location button
     */
    @Test
    public void checkGetLocation() {
        //Asserts that current activity is the LogInActivity
        // because that is where we start
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "123456nnn@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456nnn");
        solo.clickOnButton("Login");
        //After logIn we go to the Main Page
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        //From Main Page, we need to click on Habits events
        // So that we can see all habit events

        solo.clickOnView(solo.getView(R.id.navigation_habitEvent));
        solo.clickOnText("Habit Event");


        //Asserts that current activity is the HabitEventListActivity
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

        // get the first habit event
        // only habit in the one
        //assert in correct Activity
        HabitEventListActivity activity = (HabitEventListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.lv_habit_event);
        HabitEvent newHabE = (HabitEvent) listView.getItemAtPosition(0);
        String eventName = newHabE.getEventTitle();


        solo.clickOnText(eventName);

        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);

        solo.clickOnText("get location");
        solo.assertCurrentActivity("Wrong Activity", HabitEventEditMapActivity.class);

        // wait for 10 seconds to allow the location information to pop up
        solo.sleep(10000);

        solo.clickOnText("confirm");
        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);

        solo.sleep(5000);

        HabitEventEditActivity activity2 = (HabitEventEditActivity) solo.getCurrentActivity();

        EditText locationEditText = activity2.findViewById(R.id.habitEvent_enterLocation_editText);

        assertNotNull(locationEditText.getText());
    }

    /**
     * Test whether we can successfully enter the upload photo page
     */
    @Test
    public void checkUploadImage() {
        //Asserts that current activity is the LogInActivity
        // because that is where we start
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "123456nnn@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456nnn");
        solo.clickOnButton("Login");
        //After logIn we go to the Main Page
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        //From Main Page, we need to click on Habits events
        // So that we can see all habit events

        solo.clickOnView(solo.getView(R.id.navigation_habitEvent));
        solo.clickOnText("Habit Event");


        //Asserts that current activity is the HabitEventListActivity
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

        // get the first habit event
        // only habit in the one
        //assert in correct Activity
        HabitEventListActivity activity = (HabitEventListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.lv_habit_event);
        HabitEvent newHabE = (HabitEvent) listView.getItemAtPosition(0);
        String eventName = newHabE.getEventTitle();


        solo.clickOnText(eventName);

        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);

        solo.clickOnText("Upload Photo");
    }

    /**
     * Check whether adding comment functions properly
     */
    @Test
    public void checkComment() {
        //Asserts that current activity is the LogInActivity
        // because that is where we start
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "123456nnn@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456nnn");
        solo.clickOnButton("Login");
        //After logIn we go to the Main Page
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        //From Main Page, we need to click on Habits events
        // So that we can see all habit events

        solo.clickOnView(solo.getView(R.id.navigation_habitEvent));
        solo.clickOnText("Habit Event");


        //Asserts that current activity is the HabitEventListActivity
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

        // get the first habit event
        // only habit in the one
        //assert in correct Activity
        HabitEventListActivity activity = (HabitEventListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.lv_habit_event);
        HabitEvent newHabE = (HabitEvent) listView.getItemAtPosition(0);
        String eventName = newHabE.getEventTitle();


        solo.clickOnText(eventName);

        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);

        HabitEventEditActivity activity2 = (HabitEventEditActivity) solo.getCurrentActivity();
        EditText commentEditText = activity2.findViewById(R.id.habitEvent_comment_editText);

        String currentComment = commentEditText.getText().toString();
        currentComment += "111111";

        solo.enterText(commentEditText, "111111");

        // click on confirm button
        solo.clickOnText("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

        solo.clickOnText(eventName);
        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);

        solo.sleep(5000);


        assertEquals(currentComment, commentEditText.getText().toString());

        solo.clearEditText((EditText) solo.getView(R.id.habitEvent_comment_editText));

        solo.clickOnText("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}


