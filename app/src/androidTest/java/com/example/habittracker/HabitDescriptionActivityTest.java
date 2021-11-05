package com.example.habittracker;

import android.app.Activity;
import android.graphics.Point;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test for HabitDescriptionActivity
 */

public class HabitDescriptionActivityTest {
    private  Solo solo;

    @Rule
    public ActivityTestRule<HabitDescriptionActivity> rule =
            new ActivityTestRule<>(HabitDescriptionActivity.class, true, true);

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
     * Check to see the description of an activity
     */

    @Test
    public void checkHabit(){
        //Asserts that current activity is the LogInActivity
        // because that is where we start
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "test@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456");
        solo.clickOnButton("Sign In");
        //After logIn we go to the Main Page
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        //From Main Page, we need to click on Habits
        // So that we can see all habits listed
        solo.clickOnButton("Habit");
        solo.waitForActivity(HabitDescriptionActivity.class);
        //if user click on return button
        // return to the HabitListActivity
        solo.clickOnButton("Return");
        solo.waitForActivity(HabitListActivity.class);
        //if user click the delete button
        //a dialog pops out
        //check the dialog
        solo.clickOnButton("Delete");

        solo.clickOnView(solo.getView(R.id.tv_delete_confirm));

        //if user click on edit button
        // it goes to HabitEditActivity
        solo.clickOnButton("Edit");
        solo.waitForActivity(HabitEditActivity.class);



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
