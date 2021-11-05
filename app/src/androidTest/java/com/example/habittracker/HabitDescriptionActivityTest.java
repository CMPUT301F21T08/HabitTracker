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

import static org.junit.Assert.assertEquals;

/**
 * Test for HabitDescriptionActivity
 * We start from HabitList because initially, its empty
 */
@RunWith(AndroidJUnit4.class)
public class HabitDescriptionActivityTest {
    private  Solo solo;

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


    /**
     * Helper function to add Habit
     */

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
    /**
     * Check to see if the description of an activity is correct
     */
    @Test
    public void checkHabitIsCorrect(){
        //Here we check if we are on the Main Page Activity
        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);
        // add Habit
        helperAddHabit();
        // now we are back in HabitList Page
        //click on the Habit
        HabitListActivity activity = (HabitListActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.allHabits_habitList_listView);
        // click on the Habit
        solo.clickInList(0,0);
        // check information is the same.
        solo.waitForActivity("HabitDescriptionActivity");
        // now get info

        assertEquals(solo.getText(R.id.TitleInput),"Habit1" );
        assertEquals(solo.getText(R.id.dateInput),"2021-11-06" );
        assertEquals(solo.getText(R.id.frequencyInput),"2" );
        assertEquals(solo.getText(R.id.contentInput),"This is a test");
        assertEquals(solo.getText(R.id.reasonInput),"Started for testing");
        // done

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
