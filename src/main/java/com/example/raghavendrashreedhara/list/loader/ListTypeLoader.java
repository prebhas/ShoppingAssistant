package com.example.raghavendrashreedhara.list.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import com.example.raghavendrashreedhara.list.data.ListContract;
import com.example.raghavendrashreedhara.list.utility.Constants;
import com.example.raghavendrashreedhara.list.utility.ListItem;

import java.util.ArrayList;

/**
 * Created by root on 8/19/15.
 */
public class ListTypeLoader extends AsyncTaskLoader<Cursor> {
    private Context mContext;
    private int mId;
    private String[] mDates;

    public ListTypeLoader(Context context, int id, String[] dates) {

        super(context);
        mContext = context;
        mId = id;
        mDates = dates;
       // Log.i(Constants.LOG_TAG,"4.in lists type loader :: id of the loader "+id+" dates "+dates[0]);


    }

    @Override
    public Cursor loadInBackground() {
       if (mId == Constants.DEFAULT_LIST_LOADER) {
            return mContext.getContentResolver().query(
                    ListContract.ListEntry.CONTENT_URI,
                    new String[]{ListContract.ListEntry.LIST_TITLE, ListContract.ListEntry.DATE},
                    ListContract.ListEntry.IS_DEFAULT + " =? ",
                    new String[]{"1"},
                    ListContract.ListEntry.DATE+" DESC");
        }
        else {
            // if mdates is null then just come out of the loader
            if(mDates == null)
                return null;

            // SInce we are passing dynamic multiple parameters we use TextUtils class to create the query
            return mContext.getContentResolver().query(
                    ListContract.ListItemsEntry.CONTENT_URI,
                    new String[]{ListContract.ListItemsEntry._ID,
                            ListContract.ListItemsEntry.LIST_ITEMS},
                    ListContract.ListItemsEntry.DATE + " IN (" + TextUtils.join(",", mDates) + ")",
                    null,
                    ListContract.ListEntry.DATE+" DESC");
        }


    }
}