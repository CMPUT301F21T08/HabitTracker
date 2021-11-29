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

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)

public class FollowingTest {
    private Solo solo;
    public static final float NAVIGATION_X_FOLLOWING = 648;

    public static final float NAVIGATION_Y = 1994;


    // We created user with info userName 'Test', email 'test@gmail.com', password '123456'

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

    @Test
    public void FollowingTest() {
        //checking that its the correct activty
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "testing@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "Password1");
        solo.clickOnButton("Login");
        //check that we are in our MainPageActivity
        solo.waitForActivity("MainPageActivity");
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnScreen(NAVIGATION_X_FOLLOWING, NAVIGATION_Y);
        solo.waitForActivity("FollowingActivity");
        FollowingActivity activity = (FollowingActivity) solo.getCurrentActivity();
        //get listView
        ListView listView = activity.findViewById(R.id.following_listview);
        // we already have manually added a friend called Demo
        // test that its correct
        Personal_info friend = (Personal_info) listView.getItemAtPosition(0);// only Demo is there
        assertEquals((friend.getName()), "Demo");

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


