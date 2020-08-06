package com.example.suomitunti;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void timeGeneratorTest() {
        int[] times = new int[20];
        Calendar testDay = Calendar.getInstance();
        for(int i = 0; i < 20; i++) {
            times[i] = TimeFromDateGenerator.generateStartHourForDay(testDay);
            System.out.println("1) For day: " + testDay.get(Calendar.DAY_OF_YEAR) + " time is: " + times[i]);
            testDay.roll(Calendar.DAY_OF_YEAR, true);
        }
        testDay = Calendar.getInstance();
        for(int i = 0; i < 20; i++) {
            assertEquals(times[i], TimeFromDateGenerator.generateStartHourForDay(testDay));
            System.out.println("2) For day: " + testDay.get(Calendar.DAY_OF_YEAR) + " time is: " + times[i]);
            testDay.roll(Calendar.DAY_OF_YEAR, true);
        }
    }
}