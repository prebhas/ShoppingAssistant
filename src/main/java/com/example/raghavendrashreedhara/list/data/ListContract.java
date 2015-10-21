package com.example.raghavendrashreedhara.list.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * List Contract
 */
public class ListContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.raghavendrashreedhara.list";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, com.example.raghavendrashreedhara.list/list/ is a valid path for
    // looking at list data.
    public static final String PATH_LIST = "list";
    public static final String PATH_LIST_ITEMS = "list_items";



    public static final class ListEntry implements BaseColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LIST;

        // The table name for adding list
        public static final String TABLE_NAME = "list";
        // The column names of the list content table
        public static final String _ID = "_id";
        public static final String LIST_TITLE = "list_title";

        public static final String IS_DEFAULT = "is_default";
        public static final String DATE = "created_date";
        public static final String REMINDER_ALARM_DATE="reminder_alarm_date";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LIST).build();

        public static Uri buildList(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the lisitems table */
    public static final class ListItemsEntry implements BaseColumns {

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LIST;
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LIST_ITEMS;


        // The table name for adding list
        public static final String TABLE_NAME = "list_items";
        public static final String LIST_ITEMS = "list_item";
        public static final String ITEM_BRAND="brand";
        public static final String QUANTITY = "qty";
        public static final String WEIGHT = "weight";
        public static final String PRICE ="price";
        public static final String COMMENTS ="comments";
        public static final String IS_END_OF_LIST="is_end_of_list";
        public static final String DATE = ListEntry.DATE;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LIST_ITEMS).build();

        public static Uri buildListItems(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
