



package com.example.habittracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.test.core.app.ApplicationProvider;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Test class for HabitEditActivityTest.
 */
@RunWith(AndroidJUnit4.class)
public class HabitEditActivityTest {

    private Solo solo;


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
        solo.clickOnButton("CONFIRM");
    }

    @Test
    public void testAddHabit(){
        //assert we are in the correct page
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        // now add a habit
        helperAddHabit();
        // now we are back to HabitList Activity. We should also see the habit listed.
        // wait for HabitListActivity to load
        solo.waitForActivity("HabitListActivity");
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        //wait until the ListView is loaded
        solo.waitForView(R.id.allHabits_habitList_recycleView);
        //assert in correct Activity
        HabitListActivity activity = (HabitListActivity) solo.getCurrentActivity();
        // click on the Habit
        solo.clickInRecyclerView(0,0);
        // check information is the same.
        solo.waitForActivity("HabitDescriptionActivity");
        // now get info
        assertEquals(solo.getText(R.id.description_habitTitle_textView),"Habit1" );
    }

    @Test
    public void testEditHabit(){
        //assert we are in the correct page
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        //add a habit with title 'Habit1'
        helperAddHabit();
        // test the title is correct
        // wait for HabitListActivity to load
        solo.waitForActivity("HabitListActivity");
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        //wait until the ListView is loaded
        solo.waitForView(R.id.allHabits_habitList_recycleView);
        // now we will edit it
        solo.clickInRecyclerView(0,0);
        // wait for the page
        solo.waitForActivity("HabitDescriptionActivity");
        solo.waitForView(R.id.description_edit_button);
        solo.clickOnButton("EDIT");
        // wait for the EditText to be ready
        solo.waitForActivity("HabitEditActivity");
        solo.waitForView(R.id.TitleInput);
        // change the habit name
        solo.enterText((EditText) solo.getView(R.id.TitleInput), "2");
        solo.clickOnButton("CONFIRM");
        solo.sleep(10000);
        solo.waitForActivity("HabitListActivity");
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);

        //wait until the ListView is loaded
        solo.waitForView(R.id.allHabits_habitList_recycleView);
        //assert in correct Activity
        //get the habit. should have new name
        solo.clickInRecyclerView(0,0);
        // check information is the same.
        solo.waitForActivity("HabitDescriptionActivity");
        // now get info
        assertEquals(solo.getText(R.id.description_habitTitle_textView),"Habit12");
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

