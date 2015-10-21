package com.example.raghavendrashreedhara.list.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.example.raghavendrashreedhara.list.data.ListContract;
import com.example.raghavendrashreedhara.list.utility.Constants;

/**
 * loader to load the lists
 */
public class ListsLoader extends AsyncTaskLoader<Cursor> {
    private Context mContext;
    private int mId;

    public ListsLoader(Context context, int id) {
        super(context);
        mContext = context;
        mId = id;

    }

    @Override
    public Cursor loadInBackground() {
        if (mId == Constants.LIST_LOADER || mId == Constants.LIST_NAVIGATION_DRAWER)
            return mContext.getContentResolver().query(
                    ListContract.ListEntry.CONTENT_URI,
                    new String[]{ListContract.ListEntry.LIST_TITLE, ListContract.ListEntry.DATE,ListContract.ListEntry.REMINDER_ALARM_DATE},
                    null,
                    null,
                    ListContract.ListEntry.DATE+" DESC");
        else
            return mContext.getContentResolver().query(
                    ListContract.ListItemsEntry.CONTENT_URI,
                    new String[]{ListContract.ListItemsEntry._ID,
                            ListContract.ListItemsEntry.LIST_ITEMS, ListContract.ListItemsEntry.DATE},
                    null,
                    null,
                    ListContract.ListEntry.DATE+" DESC");
    }
}
