package com.example.habittracker;


import android.app.Activity;
import android.util.Log;
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


    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }




    @Test
    /**
     * It checks that the profile test dispays correct information
     * It fails for some reason.
     */
    public void checkInformationDisplayed(){
        //checking that its the correct activity
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "testing@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "Password1");
        solo.clickOnButton("Login");
        //check that we are in our MainPageActivity
        solo.waitForActivity("MainPageActivity", 1500);
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        // go to profile
        solo.clickOnScreen(NAVIGATION_X_SETTINGS, NAVIGATION_Y);
        solo.waitForActivity("ProfileActivity");
        solo.waitForView(R.id.profile_userEmail_EditText);
        solo.sleep(3000);
        String email = solo.getEditText(1).getText().toString();
        Log.v("TAG", email);
        assertEquals(email, "testing@gmail.com");
        String name = solo.getEditText(2).getText().toString();
        assertEquals(name, "Test");
    }



    @Test
    /**
     * This test checks whether the signUp button works and we can successly log out
     */
    public void signOut (){

        //checking that its the correct activity
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_useremail_editText), "testing@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password_editText), "Password1");
        solo.clickOnButton("Login");
        //check that we are in our MainPageActivity
        solo.waitForActivity("MainPageActivity", 1500);
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        // go to profile
        solo.clickOnScreen(NAVIGATION_X_SETTINGS, NAVIGATION_Y);
        solo.waitForActivity("ProfileActivity");

        solo.clickOnButton("Sign Out");

        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
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



