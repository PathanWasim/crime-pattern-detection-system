package com.crimedetect.utils;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtils {
    public static String getMonthName(int month) {
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}