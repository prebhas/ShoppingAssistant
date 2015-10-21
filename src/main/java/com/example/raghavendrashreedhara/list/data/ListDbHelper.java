package com.example.raghavendrashreedhara.list.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.raghavendrashreedhara.list.utility.Constants;

/**
 * Created by raghavendrashreedhara on 5/12/15.
 */
public class ListDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "list.db";

    public ListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_LIST_TABLE = "CREATE TABLE " + ListContract.ListEntry.TABLE_NAME + " (" +
                ListContract.ListEntry._ID + " INTEGER PRIMARY KEY," +
                ListContract.ListEntry.LIST_TITLE + " TEXT  NOT NULL, " +
                ListContract.ListEntry.IS_DEFAULT + " INTEGER NOT NULL, " +
                ListContract.ListEntry.DATE + " REAL NOT NULL" +


                " );";
        final String SQL_CREATE_LIST_ITEMS_TABLE = "CREATE TABLE " + ListContract.ListItemsEntry.TABLE_NAME + " (" +
                ListContract.ListItemsEntry._ID + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
                ListContract.ListItemsEntry.DATE + " REAL NOT NULL ," +
                ListContract.ListItemsEntry.LIST_ITEMS + " TEXT  NOT NULL, " +
                ListContract.ListItemsEntry.WEIGHT + " REAL , " +
                ListContract.ListItemsEntry.QUANTITY + " REAL , " +
                ListContract.ListItemsEntry.ITEM_BRAND + " TEXT , " +
                ListContract.ListItemsEntry.PRICE + " TEXT , " +
                ListContract.ListItemsEntry.COMMENTS + " TEXT , " +
                ListContract.ListItemsEntry.IS_END_OF_LIST+" TEXT , " +


                // Set up the date  column as a foreign key to LIST table.
                " FOREIGN KEY (" + ListContract.ListItemsEntry.DATE + ") REFERENCES " +
                ListContract.ListEntry.TABLE_NAME + " (" + ListContract.ListEntry.DATE + ")); ";


        //Create the list table
        db.execSQL(SQL_CREATE_LIST_TABLE);
        db.execSQL(SQL_CREATE_LIST_ITEMS_TABLE);
        Log.d(Constants.LOG_TAG, "create list items table " + SQL_CREATE_LIST_ITEMS_TABLE);

    }


    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + ListContract.ListEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ListContract.ListItemsEntry.TABLE_NAME);
        onCreate(db);
    }
}
