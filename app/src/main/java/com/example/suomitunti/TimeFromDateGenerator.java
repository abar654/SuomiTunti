package com.example.suomitunti;

import java.util.Calendar;
import java.util.Random;

public abstract class TimeFromDateGenerator {

    private final static int MIN_HOUR = 8;
    private final static int MAX_HOUR = 22;

    // Returns the hour which is associated with the given day
    public static int generateStartHourForDay(Calendar day) {
        Random rand = new Random();
        rand.setSeed(day.get(Calendar.DAY_OF_YEAR));
        return MIN_HOUR + rand.nextInt(MAX_HOUR - MIN_HOUR + 1);
    }
}
