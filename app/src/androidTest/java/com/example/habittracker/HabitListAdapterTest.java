

package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class HabitListAdapterTest {

    private HabitListAdapter testAdapter;
    private ArrayList<Habit> testList;
    private Habit testHabit;

    @Before
    public void setUp() {

        testList = new ArrayList<>();

        ArrayList<Integer> testOccurrenceDay = new ArrayList<Integer>();
        testOccurrenceDay.add(1);
        // Create mock habit event
        testHabit = new Habit("HabitTest1","reason1","content1",
                "2021-11-23",3,"per day",testOccurrenceDay,"1111-3333", 0, false);

        testList.add(testHabit);
        testAdapter = new HabitListAdapter(ApplicationProvider.getApplicationContext(), testList);
        // Habit(String habitTitle, String habitReason, String habitContent, String startDate, int frequency, String frequencyType, ArrayList<Integer> occurrenceDay, String uuid)

    }

    @Test
    public void testAdapter() {
        RecyclerView habitView = new RecyclerView(ApplicationProvider.getApplicationContext());
        habitView.setLayoutManager(new LinearLayoutManager(ApplicationProvider.getApplicationContext()));
        HabitListAdapter.HabitViewHolder viewHolder = testAdapter.onCreateViewHolder(habitView, 0);
        testAdapter.onBindViewHolder(viewHolder, 0);
        assertNotNull(viewHolder.habitTitleView);
        assertEquals(testHabit.getHabitTitle(), viewHolder.habitTitleView.getText());
    }
}
