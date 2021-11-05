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

@RunWith(AndroidJUnit4.class)
public class SignUpTest{
    private Solo solo;

    @Rule
    public ActivityTestRule<SignUpActivity> rule =
            new ActivityTestRule<>(SignUpActivity.class, true, true);

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
     * Check that the activity works as expected. Entering the information allows us to go to the login Activity
     * Creates user with userName 'Test', email 'test@gmail.com', password '123456'
     */
    public void checkSignup(){
        //checking that its the correct activty
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.signUp_userName_EditView), "Test");
        solo.enterText((EditText) solo.getView(R.id.signUp_userEmail_EditView), "test@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signUp_passWord_EditView), "Password");
        solo.enterText((EditText) solo.getView(R.id.signUp_confirm_passWord_EditView), "Password");
        solo.clickOnButton("Create Account");
        // expect to be successful and be directed to the the loginAcvitvity page.
        // now checking if we are in the LoginActivity page
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
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


