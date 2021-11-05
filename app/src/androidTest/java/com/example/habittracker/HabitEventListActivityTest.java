package com.example.habittracker;

import android.app.Activity;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

/**
 * Test class for Habit Event List Activity
 */

public class HabitEventListActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<HabitEventListActivity> rule =
            new ActivityTestRule<>(HabitEventListActivity.class, true, true);

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
     * check if we can add a habit event to the habit event page
     */
    @Test
    public void checkList() {
        //Asserts that current activity is the LogInActivity
        // because that is where we start
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "test@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456");
        solo.clickOnButton("Sign In");
        //After logIn we go to the Main Page
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        //From Main Page, we need to click on Habits events
        // So that we can see all habit events
        solo.clickOnButton("Habit Event");

        //User can delete a habit by clicking on the icon
        ////if user click the delete button
        //a dialog pops out
        //check the dialog

        solo.clickOnButton("Delete");
        solo.clickOnView(solo.getView(R.id.tv_delete_confirm));


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
