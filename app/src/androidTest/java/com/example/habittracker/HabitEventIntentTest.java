package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        solo.clickOnView(solo.getView(R.id.navigation_habit));
//        solo.clickOnText("Habit");
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);



        solo.sleep(5000);
        FloatingActionButton floatingActionButton = (FloatingActionButton) solo.getView(R.id.allHabits_addButton_button);
        solo.clickOnView(floatingActionButton);
        solo.sleep(5000);

        solo.assertCurrentActivity("Wrong Activity", HabitEditActivity.class);
        HabitEditActivity habitEditActivity = (HabitEditActivity) solo.getCurrentActivity();
        EditText titleText = habitEditActivity.findViewById(R.id.TitleInput);




        solo.enterText((EditText) solo.getView(R.id.TitleInput), "Habit1");//habit title
        solo.enterText((EditText) solo.getView(R.id.dateInput), "2021-11-06");// startDate
        solo.pressSpinnerItem(0, 1);// for daily
        solo.enterText((EditText) solo.getView(R.id.frequencyInput), "1");//times per day
        solo.enterText((EditText) solo.getView(R.id.contentInput), "This is a test");
        solo.enterText((EditText) solo.getView(R.id.reasonInput), "Started for testing");
        solo.clickOnButton("CONFIRM");

        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_homePage));
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);

        solo.sleep(5000);
        solo.clickOnView(solo.getView(R.id.done_button));

        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);
        solo.clickOnText("CONFIRM");


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
     * check if we can edit a habit event and confirm to the habit event page
     * check if we can delete a habit event
     */
    @Test
    public void checkConfirmAndDelete() {
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



        // delete part

        HabitEventListActivity activity3 = (HabitEventListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView3 = activity.findViewById(R.id.lv_habit_event);
        HabitEvent newHabE3 = (HabitEvent) listView3.getItemAtPosition(0);
        String eventName3 = newHabE3.getEventTitle();


        solo.clickOnText(eventName3);


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
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);

        // click on delete event button





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


