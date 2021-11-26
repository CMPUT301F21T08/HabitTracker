package com.example.habittracker;


import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;



@RunWith(AndroidJUnit4.class)
public class MainPageActivityTest{
    private Solo solo;

    public static final float NAVIGATION_X_HABIT = 0;
    public static final float NAVIGATION_X_HABITEVENT = 216;
    public static final float NAVIGATION_X_HOME = 432;
    public static final float NAVIGATION_X_FOLLOWING = 648;
    public static final float NAVIGATION_X_SETTINGS = 864;

    public static final float NAVIGATION_Y = 1994;

    // We created user with info userName 'Test', email 'test@gmail.com', password '123456'


    @Rule
    public ActivityTestRule<MainPageActivity> rule =
            new ActivityTestRule<>(MainPageActivity.class, true, true);

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


    @Test
    /**
     *Check that we go to the correct pages when we click Navigation Bar.
     */
    public void goToHabitActivity(){
        //assert that we are in MainPageActivity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnScreen(NAVIGATION_X_HABIT, NAVIGATION_Y);
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
    }

    @Test
    public void goToHabitEventActivity(){
        //assert that we are in MainPageActivity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnScreen(NAVIGATION_X_HABITEVENT, NAVIGATION_Y);
        solo.assertCurrentActivity("Wrong Activity", HabitEventListActivity.class);
    }
    @Test
    public void goToHomeActivity(){
        //assert that we are in MainPageActivity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnScreen(NAVIGATION_X_HOME, NAVIGATION_Y);
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
    }

    @Test
    public void goToFollowingActivity(){
        //assert that we are in MainPageActivity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnScreen(NAVIGATION_X_FOLLOWING, NAVIGATION_Y);
        solo.assertCurrentActivity("Wrong Activity", FollowingActivity.class);
    }

    @Test
    public void goToSettingsActivity(){
        //assert that we are in MainPageActivity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnScreen(NAVIGATION_X_SETTINGS, NAVIGATION_Y);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
    }

    private void addDailyHabit(){
        // now we click on the button
        FloatingActionButton floatingActionButton = (FloatingActionButton) solo.getView(R.id.allHabits_addButton_button);
        solo.clickOnView(floatingActionButton);
        solo.assertCurrentActivity("Wrong Activity", HabitEditActivity.class);
        solo.enterText((EditText) solo.getView(R.id.TitleInput), "Habit1");//habit title
        solo.enterText((EditText) solo.getView(R.id.dateInput), "2021-11-06");// startDate
        solo.pressSpinnerItem(0, 1);// for daily
        solo.enterText((EditText) solo.getView(R.id.frequencyInput), "2");//times per day
        solo.enterText((EditText) solo.getView(R.id.contentInput), "This is a test");
        solo.enterText((EditText) solo.getView(R.id.reasonInput), "Started for testing");
        solo.clickOnButton("Confirm");
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        // now we are back to HabitList Activity. We should also see the habit listed.
    }

    @Test
    /**
     * Check that if we add a user habit, it shows up on the main page list accordingly
     * We will test a habit with daily freq
     */
    public void checkListAdded() throws AssertionError{
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);

        // go to HabitListActivity
        solo.clickOnScreen(NAVIGATION_X_HABIT, NAVIGATION_Y);
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);

        // now we need to add a Habit. Done by the helper method. Has title "Habit1"
        addDailyHabit();
        // return to Main Activity
        solo.clickOnScreen(NAVIGATION_X_HOME, NAVIGATION_Y);
        // wait until the activity is loaded
        solo.waitForActivity("MainPageActivity");
        //wait until the ListView is loaded
        solo.waitForView(R.id.habitToDo_listView);
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        MainPageActivity activity = (MainPageActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.habitToDo_listView);
        Habit newHabit = (Habit) listView.getItemAtPosition(0);// only habit in the one
        //check that the Habit is the same as entered.
        if ((!newHabit.getHabitTitle().equals("Habit1"))) throw new AssertionError();
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



