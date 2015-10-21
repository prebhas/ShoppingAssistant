package com.example.raghavendrashreedhara.list.utility;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.raghavendrashreedhara.list.data.ListContract;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Calendar provider utils
 */
public class ContentProviderUtils {
    private Context mContext;

    public ContentProviderUtils(Context ctx) {
        mContext = ctx;
    }

    /**
     * This method inserts list items into list items table
     *
     * @param items list items
     * @param time time of insert
     */
    public void insertListitems(ArrayList<String> items, long time) {
        ContentValues listItemValues = new ContentValues();
        Gson gson = new Gson();
        String inputString = gson.toJson(items);
        listItemValues.put(ListContract.ListItemsEntry.LIST_ITEMS, inputString);
        // store todays date since that is the foreign key
        listItemValues.put(ListContract.ListItemsEntry.DATE, time);
        // Finally, insert item  data into the database.
        mContext.getContentResolver().insert(
                ListContract.ListItemsEntry.CONTENT_URI,
                listItemValues
        );
    }

    /**
     * This method inserts the list into list table
     *
     * @param listName
     * @param isDefault
     * @param time
     */
    public void insertList(String listName, boolean isDefault, long time) {
        ContentValues listValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        listValues.put(ListContract.ListEntry.LIST_TITLE, listName);
        // if boolean is set to true then set isDefault is set to 1
        listValues.put(ListContract.ListEntry.IS_DEFAULT, isDefault ? 1 : 0);
        // store todays date
        listValues.put(ListContract.ListEntry.DATE, time);
         // Finally, insert list data into the database , only one .
         mContext.getContentResolver().insert(
                ListContract.ListEntry.CONTENT_URI,
                listValues
        );
    }

    /**
     * This method inserts both the list and list values
     * @param listName
     * @param list
     * @param isdefault
     * @param time
     */
    public void insertListAndListItem(String listName,ArrayList<String> list ,boolean isdefault,long time) {
        insertList(listName, isdefault, time);
        insertListitems(list, time);
    }

    /**
     * Updates the date in the db
     *
     * @param reminderDate
     * @param listCreationDate
     */
    public void updateTheDate(long listCreationDate,long reminderDate) {
        ContentValues values = new ContentValues();
        values.put(ListContract.ListEntry.REMINDER_ALARM_DATE, reminderDate);
        int i = mContext.getContentResolver().update(
                ListContract.ListEntry.CONTENT_URI,
                values,
                ListContract.ListEntry.DATE + " =? ",
                new String[]{Long.toString(listCreationDate)}
        );
        Log.d(Constants.LOG_TAG, " update the date :: content provider " + i);
    }
}
