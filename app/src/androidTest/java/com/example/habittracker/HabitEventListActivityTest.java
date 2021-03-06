package com.example.habittracker;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;



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



// We created user with info userName 'Test', email 'test@gmail.com', password '123456'
/**
 * Test class for Habit Event List Activity
 */
@RunWith(AndroidJUnit4.class)
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


        //Asserts that current activity is the HabitEventListActivity
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);


        //assert in correct Activity
        HabitEventListActivity activity = (HabitEventListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.lv_habit_event);
        HabitEvent newHabE = (HabitEvent) listView.getItemAtPosition(0);// only habit in the one
        // check that the habit Event is correctly listed
        // this habitEvent is same as listed in the firebase
        if ((!newHabE.getEventTitle().equals("New: 11-05-2021"))) throw new AssertionError();
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
