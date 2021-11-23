package com.example.habittracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;

public class HabitEventEditActivityTest {

    Context context;
    Activity activity;
    private HabitEvent testEvent;

    ActivityScenarioRule testRule;


    @Test
    public void testGetLocalImagePath() {
        testEvent = new HabitEvent("Run", "I've finished running", "Edmonton", "1234-5678", "1111-2222");
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), HabitEventEditActivity.class);
        intent.putExtra("HabitEventForEdit", testEvent);
        intent.putExtra("EventIndex", 0);

        ActivityScenario<HabitEventEditActivity> scenario = ActivityScenario.launch(intent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);


        scenario.onActivity( activity -> {
            assertEquals(HabitEventEditActivity.class, activity.getClass());
            Uri testUri = Uri.fromFile(new File("/storage/emulated/0/Pictures/IMG_20211102_170443.jpg"));
            System.out.println(testUri);
            String localImagePath = HabitEventEditActivity.getPathFromURI(activity.getApplicationContext(), testUri);
            assertNotNull(localImagePath);
            assertEquals("/storage/emulated/0/Pictures/IMG_20211102_170443.jpg", localImagePath);
        });
        scenario.moveToState(Lifecycle.State.DESTROYED);

    }


}
