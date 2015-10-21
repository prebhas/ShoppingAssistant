package com.example.raghavendrashreedhara.list.utility;

/**
 * Created by root on 9/8/15.
 */
public class DateAndTimeUtil {
    private static int mHour;
    private static int mMinute;
    private static int mDay;
    private static int mMonth;
    private static int mYear;

    public static  void setHour(int hour) {
        mHour = hour;
    }

    public static void setMinute(int minute) {
        mMinute = minute;
    }

    public static void setDay(int day) {
        mDay = day;
    }

    public static void setMonth(int month) {
        mMonth = month;
    }

    public static void setYear(int year) {
        mYear = year;
    }



    public static int getHour() {
        return mHour;
    }

    public static int getMinute() {
        return mMinute;
    }

    public static int getDay() {
        return mDay;
    }

    public static int getMonth() {
        return mMonth;
    }

    public static int getYear() {
        return mYear;
    }



}
