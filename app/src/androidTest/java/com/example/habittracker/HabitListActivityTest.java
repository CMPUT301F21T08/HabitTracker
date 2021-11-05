package com.example.habittracker;


import android.app.Activity;
import android.util.Log;
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
public class HabitListActivityTest{
    private Solo solo;


    final float NAVIGATION_Y = 1994;

    // We created user with info userName 'Test', email 'test@gmail.com', password '123456'


    @Rule
    public ActivityTestRule<HabitListActivity> rule =
            new ActivityTestRule<>(HabitListActivity.class, true, true);

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


    private void helperAddHabit(){
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
    }

    // this is not working as I dont know how to interact with Fragment
    private void helperDeleteHabit(){
        solo.clickInList(0, 0);
        // wait for the page
        solo.waitForActivity("HabitDescriptionActivity");
        solo.waitForView(R.id.description_edit_button);
        solo.clickOnButton("Delete");
        // wait for the EditText to be ready
        solo.waitForFragmentByTag("Are you sure you want to delete?");
        solo.clickOnButton(1);
    }

    @Test
    public void addDailyHabit(){
            //assert we are in the correct page
             solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
            // now add a habit
            helperAddHabit();
            // now we are back to HabitList Activity. We should also see the habit listed.
            // wait for HabitListActivity to load
            solo.waitForActivity("HabitListActivity");
            solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);

            //wait until the ListView is loaded
            solo.waitForView(R.id.allHabits_habitList_listView);
            //assert in correct Activity
            HabitListActivity activity = (HabitListActivity) solo.getCurrentActivity();
            //get listView
            ListView listView = activity.findViewById(R.id.allHabits_habitList_listView);
            Habit newHabit = (Habit) listView.getItemAtPosition(0);// only habit in the one
            //check that the Habit is the same as entered.
            if ((!newHabit.getHabitTitle().equals("Habit1"))) throw new AssertionError();
    }


    @Test
    /**
     * Edit Habit Functionality works when you do it on your own.
     * However, when automated, there is some concurrency error.
     * We get an extra Habit rather than edited.
     */
    public void editDailyHabit(){
        //assert we are in the correct page
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        //add a habit with title 'Habit1'
        helperAddHabit();
        // test the title is correct
        // wait for HabitListActivity to load
        solo.waitForActivity("HabitListActivity");
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        //wait until the ListView is loaded
        solo.waitForView(R.id.allHabits_habitList_listView);
        //assert in correct Activity
        HabitListActivity activity = (HabitListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.allHabits_habitList_listView);
        Habit newHabit = (Habit) listView.getItemAtPosition(0);// only habit in the one
        //check that the Habit is the same as entered.
        if ((!newHabit.getHabitTitle().equals("Habit1"))) throw new AssertionError();

        // now we will edit it
        solo.clickInList(0, 0);
        // wait for the page
        solo.waitForActivity("HabitDescriptionActivity");
        solo.waitForView(R.id.description_edit_button);
        solo.clickOnButton("Edit");
        // wait for the EditText to be ready
        solo.waitForActivity("HabitEditActivity");
        solo.waitForView(R.id.TitleInput);
        // change the habit name
        solo.enterText((EditText) solo.getView(R.id.TitleInput), "2");
        solo.clickOnButton("Confirm");
        solo.sleep(10000);
        solo.waitForActivity("HabitListActivity");
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);

        //wait until the ListView is loaded
        solo.waitForView(R.id.allHabits_habitList_listView);
        //assert in correct Activity
        //get the habit. should have new name
        newHabit = (Habit) listView.getItemAtPosition(0);// only habit in the one
        //check that the Habit is the same as the edited one
        if ((!newHabit.getHabitTitle().equals("Habit12"))) throw new AssertionError();

    }

    /**
     * This will fail because the helper function Delete Habit is not working
     */
    @Test
    public void deleteDailyHabit(){
        //assert we are in the correct page
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        //add a habit with title 'Habit1'
        helperAddHabit();
        // test the title is correct
        // wait for HabitListActivity to load
        helperDeleteHabit();
        solo.sleep(10000);
        solo.waitForActivity("HabitListActivity");
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);

        //wait until the ListView is loaded
        solo.waitForView(R.id.allHabits_habitList_listView);
        //assert in correct Activity
        HabitListActivity activity = (HabitListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.allHabits_habitList_listView);

        //check that there is nothing left
        try {
            Habit newHabit = (Habit) listView.getItemAtPosition(0);// only habit in the one
        }
        catch (IndexOutOfBoundsException e){
            Log.v("Success: ", e.toString());
        }

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



