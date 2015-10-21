package com.example.raghavendrashreedhara.list.utility;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.raghavendrashreedhara.list.data.ListContract;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 *Testing class which will be removed before uploading the app in the market
 */
public class TestListAndListItemsCreate {

    /*
    TODO:remove this method once the app works fine since this is test function which inserts items into
    the list items and list db
     */
    public static void testInsertList(Context ctx) {
        // Now that the content provider is set up, inserting rows of data is pretty simple.
        // First create a ContentValues object to hold the data you want to insert.
        ContentValues listValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.

        listValues.put(ListContract.ListEntry.LIST_TITLE, "walmart");

        //Yes , the list is default so it is set to 1
        listValues.put(ListContract.ListEntry.IS_DEFAULT, 1);
        // store todays date
        listValues.put(ListContract.ListEntry.DATE, "1435932756741");


        // Finally, insert list data into the database , only one .
        Uri insertedUri = null;
        ctx.getContentResolver().insert(
                ListContract.ListEntry.CONTENT_URI,
                listValues
        );
        listValues.clear();

        // Adding the second test data
        listValues.put(ListContract.ListEntry.LIST_TITLE, "Costco ");

        // No , the list is not default so it is set to 0
        listValues.put(ListContract.ListEntry.IS_DEFAULT, 0);
        // store todays date
        listValues.put(ListContract.ListEntry.DATE, "1435932756742");


        // Finally, insert list data into the database , only one .

        ctx.getContentResolver().insert(
                ListContract.ListEntry.CONTENT_URI,
                listValues
        );
        listValues.clear();

        // Adding the third test data
        listValues.put(ListContract.ListEntry.LIST_TITLE, "Kroger ");

        //Yes , the list is  default so it is set to 1
        listValues.put(ListContract.ListEntry.IS_DEFAULT, 1);
        // store todays date
        listValues.put(ListContract.ListEntry.DATE, "1435932756743");


        // Finally, insert list data into the database , only one .

        ctx.getContentResolver().insert(
                ListContract.ListEntry.CONTENT_URI,
                listValues
        );

    }

    public static void testInsertListItems(Context ctx) {

        // Adding the list items to walmart list
        ContentValues listItemValues = new ContentValues();
        ArrayList<String> inputListArray = new ArrayList<>();
        inputListArray.add("Avacado");
        inputListArray.add("Buttermilk");
        inputListArray.add("Strawberry Jam");

        Gson gson = new Gson() ;
        String inputString = gson.toJson(inputListArray);
        Log.i(Constants.LOG_TAG,"The input string is "+inputString);



        listItemValues.put(ListContract.ListItemsEntry.LIST_ITEMS, inputString);


        // store todays date since that is the foreign key
        listItemValues.put(ListContract.ListItemsEntry.DATE, "1435932756741");


        // Finally, insert item  data into the database.
         ctx.getContentResolver().insert(
                 ListContract.ListItemsEntry.CONTENT_URI,
                 listItemValues
         );

        listItemValues.clear();
        inputListArray.clear();
        ///////////////////////////////////////////////////////////////////////////////////////////

        // Enter first item in "COSTCO" list name

        inputListArray.add("Milk");
        inputListArray.add("Eggs");
        inputListArray.add("Butter");
        inputListArray.add("Banana");


         inputString = gson.toJson(inputListArray);
        Log.i(Constants.LOG_TAG, "The input string is " + inputString);

        listItemValues.put(ListContract.ListItemsEntry.LIST_ITEMS, inputString);


        // store todays date since that is the foreign key
        listItemValues.put(ListContract.ListItemsEntry.DATE, "1435932756742");
        listItemValues.put(ListContract.ListItemsEntry.IS_END_OF_LIST, "false");


        // Finally, insert item  data into the database.
        ctx.getContentResolver().insert(
                ListContract.ListItemsEntry.CONTENT_URI,
                listItemValues
        );

       inputListArray.clear();
        listItemValues.clear();

        ///////////////////////////////////////////////////////////////////////////////////////////

        // KROGER list
        inputListArray.add("Olive oil ");
        inputListArray.add("Sushi");
        inputListArray.add("Baby wipes");
        inputListArray.add("Gas");


        inputString = gson.toJson(inputListArray);
        Log.i(Constants.LOG_TAG, "The input string is " + inputString);

        listItemValues.put(ListContract.ListItemsEntry.LIST_ITEMS, inputString);


        // store todays date since that is the foreign key
        listItemValues.put(ListContract.ListItemsEntry.DATE, "1435932756743");

        // Finally, insert item  data into the database.
        Uri uri = ctx.getContentResolver().insert(
                ListContract.ListItemsEntry.CONTENT_URI,
                listItemValues
        );
        Log.i(Constants.LOG_TAG,"the inserted URI is "+uri);
        listItemValues.clear();




    }
}
