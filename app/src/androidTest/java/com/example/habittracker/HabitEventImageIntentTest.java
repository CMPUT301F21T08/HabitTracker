


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


/**
 * Test class for Habit Event List Activity
 */
@RunWith(AndroidJUnit4.class)
public class HabitEventImageIntentTest {
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


        solo.sleep(5000);
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

        solo.sleep(5000);

        solo.clickOnText(eventName);

        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);

        solo.clickOnText("Camera");
        solo.finishOpenedActivities();

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


        solo.sleep(5000);
        solo.clickOnText(eventName);

        solo.assertCurrentActivity("Wrong Activity", HabitEventEditActivity.class);

        solo.clickOnText("Upload Photo");
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


