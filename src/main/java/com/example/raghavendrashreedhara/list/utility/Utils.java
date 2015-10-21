package com.example.raghavendrashreedhara.list.utility;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.raghavendrashreedhara.list.receiver.AlarmReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * General class utils with helper functions
 */
public class Utils {


    public static ArrayList<String> returnListNames(Cursor cursor) {
        if (cursor == null)
            return null;
        ArrayList<String> listNames = new ArrayList<>(cursor.getCount());
        final int LIST_TITLE_AND_ITEMS_INDEX = 0;

        cursor.moveToFirst();
        // Add the list Names from cursor

        while (!cursor.isAfterLast()) {
            String listName = cursor.getString(LIST_TITLE_AND_ITEMS_INDEX);
            listNames.add(listName);
            cursor.moveToNext();
        }

        return listNames;
    }

    /**
     * Return the list items from given cursor
     * @param cursor
     * @param id
     * @return
     */
    public static ArrayList<String> returnListItems(Cursor cursor, int id) {
        ArrayList<String> listItems = new ArrayList<>();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        if (cursor != null) {
            cursor.moveToNext();
            if (cursor.getCount() > 0) {
                listItems = new Gson().fromJson(cursor.getString(id), type);
            }
        }
        return listItems;
    }

    /**
     * Returns the list date from the given cursor
     * @param cursor
     * @return
     */
    public static ArrayList<Long> returnListDates(Cursor cursor, int index) {
        if (cursor == null)
            return null;
        ArrayList<Long> listDates = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        // Add the list Names from cursor

        while (!cursor.isAfterLast()) {
            long listDate = cursor.getLong(index);
            //Log.i(Constants.LOG_TAG, "list name " + listDate);
            listDates.add(listDate);
            cursor.moveToNext();
        }
        return listDates;
    }

    /**
     * Set the updated time into the shared preferences
     * @param activity
     * @param time
     */
    public static void setTimeintoSharedPreferences(Activity activity, long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(Constants.UPDATED_TIME, time).apply();
    }

    /**
     * Gets the list updated time from shared preferences
     * @param activity
     * @return
     */
    public static long getTheLongUpdatedTime(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getLong(Constants.UPDATED_TIME, 0);
    }

    /**
     * Returns the date after formatting
     * @param date
     * @return
     */
    public static String returnDate(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy");
        return format.format(date);
    }

    /**
     * Simple util function which formats the time in milliseconds
     * @param time
     * @return
     */
    public static String returnCurrentTime(long time) {
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        date.setTime(time);
        String dateString = fmt.format(date);
        return dateString;
    }

    /**
     * Set the reminder intent using alarm manaager
     * @param context
     * @param cal
     * @param type
     * @param listCreationDate
     * @param listName
     */
    public static void setReminder(Context context, Calendar cal, int type, long listCreationDate, String listName) {
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
       // constants--in this case, AlarmManager.INTERVAL_DAY.
        Intent intent = new Intent(context, AlarmReceiver.class);
        ShoppingList list = new ShoppingList();
        list.setListDate(listCreationDate);
        list.setListname(listName);
        intent.putExtra(Constants.GET_PARCELABLE_SHOPPING_LIST, list);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        switch (type) {
            case 0:
                // no repeat of alarm
                alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);
                //alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmIntent);

                break;
            case 1:
                // repeat the alarm after a day
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);
                break;
            // repeat the alarm weekly
            case 2:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        24 * 60 * 60 * 1000 * 7, alarmIntent);
                break;
            // repeat the alarm monthly
            case 3:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        24 * 60 * 60 * 1000 * 30, alarmIntent);
                break;
            // repeat the alarm yearly
            case 4:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        24 * 60 * 60 * 1000 * 365, alarmIntent);
                break;
        }

    }

    /**
     * Create a share intent to share the grocery list
     *
     * @param listName
     * @param items
     * @return
     */
    public static Intent createShareListIntent(String listName, ArrayList<String> items) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        // TODO ? remove the deprecated code
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String listItems = "#" + listName + "\n" + "\n";
        for (String listItem : items) {
            listItems = listItems + listItem + "\n";
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, listItems);
        return shareIntent;
    }

    /**
     * This method deletes the pending intent which is linked to that particular intent
     *
     * @param ctx
     * @param alarmCreationtime
     */
    public static void cancelAlarmPendingIntent(Context ctx, long alarmCreationtime,long listcreationtime) {
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + alarmCreationtime));
        alarmIntent.setAction(String.valueOf(alarmCreationtime));
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        PendingIntent displayIntent = PendingIntent.getBroadcast(ctx, 0, alarmIntent, 0);
        displayIntent.cancel();
        alarmManager.cancel(displayIntent);
        // Update the reminder time in db
        ContentProviderUtils utils = new ContentProviderUtils(ctx);
        utils.updateTheDate(listcreationtime, alarmCreationtime);
    }

}
