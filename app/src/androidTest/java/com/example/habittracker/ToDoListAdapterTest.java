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

public class ToDoListAdapterTest {

    private ToDoListAdapter testAdapter;
    private ArrayList<Habit> testList;
    private Habit testHabit;

    @Before
    public void setUp() {

        testList = new ArrayList<>();

        ArrayList<Integer> testOccurrenceDay = new ArrayList<Integer>();
        testOccurrenceDay.add(1);
        // Create mock habit event
        testHabit = new Habit("HabitTest1","reason1","content1",
                "2021-11-20",3,"per day",testOccurrenceDay,"1111-3333", 0, false);

        testList.add(testHabit);
        testAdapter = new ToDoListAdapter(ApplicationProvider.getApplicationContext(), testList);
        // Habit(String habitTitle, String habitReason, String habitContent, String startDate, int frequency, String frequencyType, ArrayList<Integer> occurrenceDay, String uuid)

    }

    @Test
    public void testGetView() {
        View view = testAdapter.getView(0, null, null);

        TextView textView = view.findViewById(R.id.toDo_habitContent_textView);
        assertNotNull(view);
        assertNotNull(textView);
        assertEquals(testHabit.getHabitTitle(), textView.getText());
    }
}
