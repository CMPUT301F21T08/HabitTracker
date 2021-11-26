package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class profileTest {

    public Personal_info info;

    @BeforeEach
    public void setup(){
        info = new Personal_info("Test","test@gmail.com","Others",1);
    }

    // getters test
    @Test
    public void getNameTest(){
        assertEquals("Test", info.getName());
    }
    @Test
    public void getEmailTest(){
        assertEquals("test@gmail.com", info.getEmail());
    }
    @Test
    public void getGenderTest(){
        assertEquals("Others", info.getGender());
    }
    @Test
    public void getAgeTest(){
        assertEquals(1, info.getAge());
    }

    // setters test
    @Test
    public void setNameTest(){
        info.setName("Test1");
        assertEquals("Test1", info.getName());
    }
    @Test
    public void setEmailTest(){
        info.setEmail("test1@gmail.com");
        assertEquals("test1@gmail.com", info.getEmail());
    }
    @Test
    public void setGenderTest(){
        info.setGender("Male");
        assertEquals("Male", info.getGender());
    }
    @Test
    public void setAgeTest(){
        info.setAge(2);
        assertEquals(2, info.getAge());
    }
}
