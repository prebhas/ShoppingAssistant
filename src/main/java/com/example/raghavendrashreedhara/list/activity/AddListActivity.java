package com.example.raghavendrashreedhara.list.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.raghavendrashreedhara.list.R;
import com.example.raghavendrashreedhara.list.data.ListContract;
import com.example.raghavendrashreedhara.list.fragment.DisplayListItemsInAddFragment;
import com.example.raghavendrashreedhara.list.loader.ListsLoader;
import com.example.raghavendrashreedhara.list.utility.Constants;
import com.example.raghavendrashreedhara.list.utility.ShoppingList;
import com.example.raghavendrashreedhara.list.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Add activity to create new list
 */
public class AddListActivity extends ActionBarActivity implements ImageChooserListener, LoaderManager.LoaderCallbacks<Cursor> {

    private AutoCompleteTextView mListTitleAtv;
    private AutoCompleteTextView mListItemNameAtv;
    private EditText mWeightTv;
    private EditText mBrandTv;

    private ImageView mSelectPic;
    private EditText mCommentsTv;
    private CheckBox mIsDefaultCtv;
    private ImageChooserManager mImageChooserManager;
    private DisplayListItemsInAddFragment mFragment;
    private long mDate = System.currentTimeMillis();
    private String mListName;

    private static final int TAKE_PICTURE_FROM_CAMERA = 0;
    private static final int TAKE_PICTURE_FROM_GALLERY = 1;
    private Uri mListInsertUri;
    private Uri mListItemsInsertUri;
    private ArrayList<String> mListItemList = new ArrayList<>();
    private String mIsLastItem = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_list);
        // display the fragment
        displayAddItemsFragment();
        // Find all the views
        mListTitleAtv = (AutoCompleteTextView) findViewById(R.id.list_title);
        mListItemNameAtv = (AutoCompleteTextView) findViewById(R.id.list_item);
        mWeightTv = (EditText) findViewById(R.id.weight);
        mBrandTv = (EditText) findViewById(R.id.brand);


        mCommentsTv = (EditText) findViewById(R.id.comments);
        mIsDefaultCtv = (CheckBox) findViewById(R.id.is_default);
        // TODO : For now this icon is going to save the item name but it is actually for adding pic about the list item
        mSelectPic = (ImageView) findViewById(R.id.add_new_item_pic);
        mSelectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = 0;
                //TODO to add a chooser images of camera and gallery
                // TODO fix images taken in camera are not stored in gallery
//                if (type == TAKE_PICTURE_FROM_CAMERA)
//                    takePicture(ChooserType.REQUEST_PICK_PICTURE);
//                else
//                    takePicture(ChooserType.REQUEST_CAPTURE_PICTURE);
                //insertListItem();

                mFragment.refresh(listDescription());
                mListItemNameAtv.setText("");

            }
        });

// set the action bar up button
        setActionBarUpButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO : these are test methods and remove them once data is stored in sql
        //testDataInsert(getApplicationContext());
        // TODO :uncomment later because this loads the list and list items for auto complete
        getSupportLoaderManager().initLoader(Constants.LIST_LOADER, null, this);
        getSupportLoaderManager().initLoader(Constants.LIST_ITEMS_LOADER, null, this);


    }

//    private void testDataInsert(Context ctx) {
//        TestListAndListItemsCreate.testInsertList(ctx);
//        TestListAndListItemsCreate.testInsertListItems(ctx);
//        finish();
//
//    }

    private void displayAddItemsFragment() {
        mFragment = new DisplayListItemsInAddFragment();
        Bundle arguments = new Bundle();
        // sending the newly added list item
        ShoppingList list = new ShoppingList();
        list.setListItems(mListItemList);
        arguments.putParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST,list);
        mFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.listitems_detail_container, mFragment, Constants.DISPLAY_LIST_ITEMS_FRAGMENT.toString())
                .commit();

    }

    private void takePicture(int type) {
        mImageChooserManager = new ImageChooserManager(this, type);
        mImageChooserManager.setImageChooserListener(this);
        try {
            mImageChooserManager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // This save item saves the list and comes out of the activity
        if (id == R.id.save_item) {
            mIsLastItem = "true";
            // insert the newly created list item
            insertList();
            insertListItem();
            // Resetting the edit text
            mListItemNameAtv.setText("");
            mWeightTv.setText("");
            finish();
            return true;
        } else if(id == R.id.home) {
            insertList();
            insertListItem();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == ChooserType.REQUEST_PICK_PICTURE ||
                        requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            mImageChooserManager.submit(requestCode, data);
        }

    }

    @Override
    public void onImageChosen(ChosenImage chosenImage) {
        if (chosenImage != null)
            mSelectPic.setImageDrawable(Drawable.createFromPath(chosenImage.getFilePathOriginal()));

    }

    @Override
    public void onError(String s) {
        Log.d(Constants.LOG_TAG, "Error on capturing image " + s);

    }


    //This method inserts the list item into the db.
    /* TODO :For now it inserts the list item individually but may be you need to
    * collect all the item values and insert it as bulk insert when the user comes out of the
    * activity
    *
    */

    @Override
    protected void onPause() {
        super.onPause();
        if (mListInsertUri == null && mListTitleAtv != null) {
            insertList();
        }
        if (mListItemsInsertUri == null && mListItemNameAtv != null)
            insertListItem();
    }

    private Uri insertListItem() {
        mListName = mListTitleAtv.getText().toString();
        CharSequence listItem = mListItemNameAtv.getText();
        if (mListName == null && listItem == null) {
            // just come out of the activity if the list title and item is null since the
            // list is empty
            //TODO :may be add a error message saying that lisn
            return null;
        }
        CharSequence isListDefault = mIsDefaultCtv.getText();
        CharSequence quantity = null;

        CharSequence brand = null;
        CharSequence comments = null;
        CharSequence weight = null;


        if (mBrandTv.getVisibility() == View.VISIBLE) {
            brand = mBrandTv.getText();
        }
        if (mCommentsTv.getVisibility() == View.VISIBLE) {
            comments = mCommentsTv.getText();
        }
        if (mWeightTv.getVisibility() == View.VISIBLE) {
            weight = mWeightTv.getText();
        }


        ContentValues listItemValues = new ContentValues();
        Gson gson = new Gson();
        String inputString = gson.toJson(mListItemList);
        if (listItem != null)
            listItemValues.put(ListContract.ListItemsEntry.LIST_ITEMS, inputString);


        if (weight != null)
            listItemValues.put(ListContract.ListItemsEntry.WEIGHT, weight.toString());
        //Insert the items now
        if (quantity != null)
            listItemValues.put(ListContract.ListItemsEntry.QUANTITY, quantity.toString());

        if (brand != null)
            listItemValues.put(ListContract.ListItemsEntry.ITEM_BRAND, brand.toString());

        if (comments != null)
            listItemValues.put(ListContract.ListItemsEntry.COMMENTS, comments.toString());

        //TODO :insert if the list is default or not
        //listItemValues.put(ListContract.ListEntry.IS_DEFAULT,)


        // store todays date since that is the foreign key
        listItemValues.put(ListContract.ListItemsEntry.DATE, mDate);
        listItemValues.put(ListContract.ListItemsEntry.IS_END_OF_LIST, mIsLastItem);
        // Finally, insert item  data into the database.
        mListItemsInsertUri = getApplicationContext().getContentResolver().insert(
                ListContract.ListItemsEntry.CONTENT_URI,
                listItemValues
        );



        return mListItemsInsertUri;
    }

    private String listDescription() {
        //the description to be displayed in display list
        CharSequence listItem = mListItemNameAtv.getText();
        String listItemDescription = listItem.toString();
        if (mWeightTv != null) {
            listItemDescription = listItemDescription + "  " + mWeightTv.getText();
        }

        mListItemList.add(listItemDescription);
        return listItemDescription;
    }

    /**
     * Inserts the grocery list into db
     * @return
     */
    private Uri insertList() {
        // Now that the content provider is set up, inserting rows of data is pretty simple.
        // First create a ContentValues object to hold the data you want to insert.
        ContentValues listValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        if (mListTitleAtv != null && mListTitleAtv.getText()!=null ) {
            String input = mListTitleAtv.getText().toString();
            // Capitalise the first letter of the string
            String listName = input.substring(0, 1).toUpperCase() + input.substring(1);
            listValues.put(ListContract.ListEntry.LIST_TITLE, listName);
        }
        else
            listValues.put(ListContract.ListEntry.LIST_TITLE, "");

        //
        listValues.put(ListContract.ListEntry.IS_DEFAULT, (mIsDefaultCtv.isChecked()) ? 1 : 0);
        // store todays date
        listValues.put(ListContract.ListEntry.DATE, mDate);
        // Finally, insert list data into the database , only one .

        mListInsertUri = getApplicationContext().getContentResolver().insert(
                ListContract.ListEntry.CONTENT_URI,
                listValues
        );

        return mListInsertUri;

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ListsLoader loader;
        if (id == Constants.LIST_LOADER)
            loader = new ListsLoader(getApplicationContext(), Constants.LIST_LOADER);
        else
            loader = new ListsLoader(getApplicationContext(), Constants.LIST_ITEMS_LOADER);
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == Constants.LIST_LOADER)
            autoPopulateListNames(data);
        else
            autoPopulateListItemNames(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void autoPopulateListNames(Cursor cursor) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Utils.returnListNames(cursor));
        mListTitleAtv.setAdapter(adapter);
    }

    private void autoPopulateListItemNames(Cursor cursor) {
        ArrayList<String> listItems = new ArrayList<>();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(1) != null && !cursor.getString(1).isEmpty()) {
                ArrayList<String> finalOutputString = new Gson().fromJson(cursor.getString(1), type);
                if (finalOutputString != null && !finalOutputString.isEmpty()) {
                    for (String listItem : finalOutputString) {
                        listItems.add(listItem);
                    }

                }
            }
            cursor.moveToNext();

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        mListItemNameAtv.setAdapter(adapter);
    }

    public void setActionBarUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
    }
}
