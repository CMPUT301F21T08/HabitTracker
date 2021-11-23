package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class HabitEventListAdapterTest {

    private HabitEventListAdapter testAdapter;
    private ArrayList<HabitEvent> testList;
    private HabitEvent testEvent;

    @Before
    public void setUp() {

        testList = new ArrayList<>();

        // Create mock habit event
        testEvent = new HabitEvent("Run", "11111", "Edmonton", "1111-2222", "3333-4444");
        testList.add(testEvent);
        testAdapter = new HabitEventListAdapter(ApplicationProvider.getApplicationContext(), testList);
    }

    @Test
    public void testGetView() {
        View view = testAdapter.getView(0, null, null);

        TextView textView = view.findViewById(R.id.tv_habit_event);
        assertNotNull(view);
        assertNotNull(textView);
        assertEquals(testEvent.getEventTitle(), textView.getText());
    }
}
