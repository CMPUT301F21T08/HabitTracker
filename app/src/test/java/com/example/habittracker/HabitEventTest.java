package com.example.habittracker;

import org.junit.Before;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HabitEventTest {
    private HabitEvent testEvent;
    private String date;

    /**
     * Construct an event object to be used for test
     */
    @BeforeEach
    public void constructEvent() {
        testEvent = new HabitEvent("Run", "I've finished running", "Edmonton", "1234-5678");
        date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    }

    /**
     * Test the getter for event title
     */
    @Test
    public void testGetEventTitle() {
        String eventTitle = "Run: "+date;
        assertEquals(eventTitle, testEvent.getEventTitle());
    }

    /**
     * Test the setter for event title
     */
    @Test
    public void testSetEventTitle() {
        testEvent.setEventTitle("Test title");
        assertEquals("Test title", testEvent.getEventTitle());
    }

    /**
     * Test the getter for comment
     */
    @Test
    public void testGetComment() {
        assertEquals("I've finished running", testEvent.getComment());
    }

    /**
     * Test setter for comment
     */
    @Test
    public void testSetComment() {
        testEvent.setComment("Test comment");
        assertEquals("Test comment", testEvent.getComment());
    }

    /**
     * Test getter for location
     */
    @Test
    public void testGetLocation() {
        assertEquals("Edmonton", testEvent.getLocation());
    }

    /**
     * Test setter for location
     */
    @Test
    public void testSetLocation() {
        testEvent.setLocation("Hamilton");
        assertEquals("Hamilton", testEvent.getLocation());
    }

    /**
     * Test getter for uuid
     */
    @Test
    public void testGetUuid() {
        assertEquals("1234-5678", testEvent.getUuid());
    }

    /**
     * Test setter for uuid
     */
    @Test
    public void testSetUuid() {
        testEvent.setUuid("4321-8765");
        assertEquals("4321-8765", testEvent.getUuid());
    }

    /**
     * Test getter for url
     */
    @Test
    public void testGetDownloadUrl(){
        assertEquals(null, testEvent.getDownloadUrl());
    }

    /**
     * Test setter for url
     */
    @Test
    public void testSetDownloadUrl() {
        testEvent.setDownloadUrl("www.google.com");
        assertEquals("www.google.com", testEvent.getDownloadUrl());
    }
}