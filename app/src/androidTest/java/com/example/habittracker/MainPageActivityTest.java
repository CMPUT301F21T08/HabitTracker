package com.example.habittracker;


import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

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
    public void NavigationBar(){
        //assert that we are in MainPageActivity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
    }




    @Test
    /**
     * Check that if we add a user habit, it shows up on the main page list accordingly
     * We will test a habit with daily freq
     */
    public void checkListAdded() throws AssertionError{
        //we need to start at the LogInActivity because its user specific
        // same code as that used for checkLoggingIn() in LogInTest.java
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "test@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456");
        solo.clickOnButton("Sign In");
        //check that we are in our MainPageActivity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);

        // now we need to add Habit1
        // get help to go to habit page using navigation bar
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        // now we click on the button
        solo.clickOnButton(R.id.allHabits_addButton_button);
        solo.assertCurrentActivity("Wrong Activity", HabitEditActivity.class);
        solo.enterText((EditText) solo.getView(R.id.TitleInput), "Habit1");//habit title
        solo.enterText((EditText) solo.getView(R.id.dateInput), "2021-11-06");// startdat
        solo.pressSpinnerItem(R.id.frequency_spinner, 1);// for daily
        solo.enterText((EditText) solo.getView(R.id.contentInput), "This is a test");
        solo.enterText((EditText) solo.getView(R.id.reasonInput), "Started for testing");
        solo.clickOnButton("Confirm");
        // now we are back to HabitList Activity. We should also see the habit listed.
        // insert code to go back to MainPageActivity
        MainPageActivity activity = (MainPageActivity) solo.getCurrentActivity();
        ListView listView = activity.findViewById(R.id.habitToDo_listView);
        Habit newHabit = (Habit) listView.getItemAtPosition(0);// only habit in the one
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



