package com.example.habittracker;


import android.app.Activity;
import android.widget.EditText;

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
public class ProfileTestActivity{
    private Solo solo;

    // currently settings page is our Profile Page

    public static final float NAVIGATION_X_SETTINGS = 864;
    public static final float NAVIGATION_Y = 1994;

    // We created user with info userName 'Test', email 'test@gmail.com', password '123456'


    @Rule
    public ActivityTestRule<LogInActivity> rule =
            new ActivityTestRule<>(LogInActivity.class, true, true);



    public void checkInformationDisplayed(){
        //checking that its the correct activty
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "test@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "123456");
        solo.clickOnButton("Sign In");
        //check that we are in our MainPageActivity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        // go to profile
        solo.clickOnScreen(NAVIGATION_X_SETTINGS, NAVIGATION_Y);
        solo.waitForActivity("ProfileActivity");

        //String email = solo.getString(R.id.profile_userEmail_TextView);
        //assertEquals(email, "test@gmail.com");

    }


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
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}



