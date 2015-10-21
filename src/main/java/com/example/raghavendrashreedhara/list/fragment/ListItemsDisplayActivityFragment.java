package com.example.raghavendrashreedhara.list.fragment;


import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.raghavendrashreedhara.list.R;
import com.example.raghavendrashreedhara.list.activity.ListItemsDisplayActivity;
import com.example.raghavendrashreedhara.list.adapter.DisplayListItemsAdapter;
import com.example.raghavendrashreedhara.list.data.ListContract;
import com.example.raghavendrashreedhara.list.utility.Constants;
import com.example.raghavendrashreedhara.list.utility.ContentProviderUtils;
import com.example.raghavendrashreedhara.list.utility.ShoppingList;
import com.example.raghavendrashreedhara.list.utility.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListItemsDisplayActivityFragment extends Fragment {

    private ListView mDisplayListItems;
    private DisplayListItemsAdapter mListAdapter;
    private EditText mAddNewItemEditlist;
    private String mListName = "default";
    private View mListViewHeader;
    private long mListDate;
    private boolean mIsActionEditClicked;
    private boolean mIsListDefault;

    private ShareActionProvider mShareActionProvider;
    MenuItem mActionDone;
    MenuItem mActionEdit;
    MenuItem mActionDelete;
    private long mReminderDate;
    private ArrayList<String> mListItems;

    public ListItemsDisplayActivityFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
        if (mActionDelete != null && mActionEdit != null && mActionDone != null)
            resetTheActionBar();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        EditText editText = (EditText) mListViewHeader.findViewById(R.id.list_name_header_editText);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_accept: {
                // If the user has edited the default list then , keep the default list and create a
                // new list with new /updated list items
                if (mIsListDefault) {
                    insertNewList(getActivity());
                    getActivity().finish();
                    return true;
                    // Here accept is called after edit button is clicked and the user is done editing the
                    // list name
                } else if (mIsActionEditClicked) {
                    mIsActionEditClicked = false;
                    if (editText.getText() != null)
                        updateTheContentProvider(null, editText.getText().toString());
                    return true;
                } else {
                    // This is called when user has been editing the list items and has pressed done button
                    // Checking if the user has added new items along with editing the existing items
                    if (mListAdapter != null) {
                        // The list is default so we create a new list with the list items
                        if (mAddNewItemEditlist != null && mAddNewItemEditlist.getText() != null) {
                            ArrayList<String> list = updateListItems();
                            updateTheContentProvider(list, null);
                        }
                    }
                    editText.setVisibility(View.GONE);
                }
            }
            break;
            case R.id.action_delete: {
                mActionDone.setVisible(true);
                if (mListAdapter != null) {
                    if (mListAdapter.getDeleteItemsIndex().isEmpty()) {
                        // Delete the list since user has not checked any list items
                        deleteList();
                    } else
                        deleteItemsFromContentProvider(new ArrayList<Integer>(mListAdapter.getDeleteItemsIndex()));
                }
            }
            break;
            case R.id.action_edit: {
                mDisplayListItems.addHeaderView(mListViewHeader);
                // Edit the list name calles, change visiblilty of the header so that the
                // header is seen
                mIsActionEditClicked = true;
                editText.setVisibility(View.VISIBLE);
                editText.setText(mListName);
                setActionbarOnEdit();
            }
            break;
            case R.id.action_set_reminder:
                if(item.getIcon().equals(R.drawable.ic_access_alarm_white_48dp)) {
                    // set the reminder and set the cancel reminder button
                    showReminderTimeDialog();
                    item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_alarm_off_white_48pt));
                } else {
                    if (mReminderDate != 0) {
                        // cancel the reminder and set the reminder button
                        Utils.cancelAlarmPendingIntent(getContext(), mListDate, mReminderDate);
                        item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_access_alarm_white_48dp));
                    }
                }


                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_items_display, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mListDate != 0) {
            mShareActionProvider.setShareIntent(Utils.createShareListIntent(mListName, mListItems));
        }
        //this is menu item for done
        mActionDone = menu.findItem(R.id.action_accept);
        mActionDone.setVisible(false);
        mActionDelete = menu.findItem(R.id.action_delete);
        mActionEdit = menu.findItem(R.id.action_edit);

        if (mReminderDate != 0) {
            MenuItem actionSetAlarm = menu.findItem(R.id.action_set_reminder);
            actionSetAlarm.setIcon(R.drawable.ic_alarm_off_white_48pt);
        }

    }


    /**
     * This method shows the time and date picker dialog fragment
     */
    private void showReminderTimeDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ShowTimeAndDateChooser frag = new ShowTimeAndDateChooser();
        ShoppingList list = new ShoppingList();
        list.setListname(mListName);
        list.setListDate(mListDate);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST, list);
        frag.setArguments(bundle);
        frag.show(fm, "fragment_edit_name");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.getArguments() != null) {
            ShoppingList slist = this.getArguments().getParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST);
            mListItems = slist.getListItems();
            mListDate = slist.getListDate();
            mListName = slist.getListname();
            mIsListDefault = slist.getisListDefault();   // Set the action bar title which is in its parent activity
            mReminderDate = slist.getReminderDate();
            ((ListItemsDisplayActivity) getActivity()).setActionBarTitle(mListName);
        }

        View view = inflater.inflate(R.layout.fragment_display_list_items, container, false);
        mDisplayListItems = (ListView) view.findViewById(R.id.display_list_items_recycleview);

        mAddNewItemEditlist = (EditText) view.findViewById(R.id.edit_text_add_list_items);
        mAddNewItemEditlist.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    setActionbarOnEdit();
            }
        });


        final TextView dummyAddNewItemEditText = (TextView) view.findViewById(R.id.edit_text_add_list_items_dummy);
        dummyAddNewItemEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddNewItemEditlist.getText() != null) {
                    mListItems.add(mAddNewItemEditlist.getText().toString());
                    mListAdapter.notifyDataSetChanged();
                    mAddNewItemEditlist.setText(null);
                }
            }
        });
        dummyAddNewItemEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mAddNewItemEditlist.getText() != null) {
                        mListItems.add(mAddNewItemEditlist.getText().toString());
                        mListAdapter.notifyDataSetChanged();
                        mAddNewItemEditlist.setText(null);
                    }
                }
            }
        });

        // Add the header , where the user can edit the list name
        mListViewHeader = inflater.inflate(R.layout.list_view_header, null);

        if (mDisplayListItems != null) {
            mListAdapter = new DisplayListItemsAdapter(getActivity(), mListItems, ListItemsDisplayActivityFragment.this);
            mDisplayListItems.setAdapter(mListAdapter);
            mListAdapter.notifyDataSetChanged();
        }
        return view;
    }

    /**
     * Updates the content provider when the user deletes the existing list items or  updates
     * the list items
     *
     * @param list
     * @param listname
     * @return
     */
    private int updateTheContentProvider(ArrayList<String> list, String listname) {
        ContentValues values = new ContentValues();
        long currentTime = System.currentTimeMillis();
        // Store the current time in shared preferences so that mainListFragment gets the
        // updated time
        Utils.setTimeintoSharedPreferences(getActivity(), currentTime);
        if (list != null) {
            Gson gson = new Gson();
            String inputString = gson.toJson(list);
            values.put(ListContract.ListItemsEntry.LIST_ITEMS, inputString);
        }
        // store todays date since that is the foreign key
        values.put(ListContract.ListItemsEntry.DATE, currentTime);
        // Finally, update  item  data into the database.
        int rowId = getActivity().getContentResolver().update(
                ListContract.ListItemsEntry.CONTENT_URI,
                values,
                ListContract.ListItemsEntry.DATE + "=?",
                new String[]{Long.toString(mListDate)}
        );
        values.clear();
        // Update the list date as well
        if (listname != null && !listname.isEmpty()) {
            values.put(ListContract.ListEntry.LIST_TITLE, listname);
        }
        values.put(ListContract.ListItemsEntry.DATE, currentTime);
        // Finally, insert item  data into the database.
        getActivity().getContentResolver().update(
                ListContract.ListEntry.CONTENT_URI,
                values,
                ListContract.ListEntry.DATE + " =? ",
                new String[]{Long.toString(mListDate)}
        );
        getActivity().finish();
        return rowId;
    }

    // Returns the list items added and updates the content provider
    public void updateList() {
        if (mAddNewItemEditlist.getText() != null) {
            String lines[] = mAddNewItemEditlist.getText().toString().split("\\r?\\n");
            for (String l : lines) {
                mListItems.add(l);
                updateTheContentProvider(mListItems, null);
            }
        }

    }


    // Delete the list items from content provider but we are updating the list items
    private void deleteItemsFromContentProvider(ArrayList<Integer> deletePositions) {
        // We are calling the "Update" content provider since we are storing the list items as json so
        // we have to create a new json string with removed items and store the items.
        ArrayList<String> items = mListItems;
        for (int pos = 0; pos < deletePositions.size(); pos++) {
            if (items != null) {
                // Updating the array list
                items.set(deletePositions.get(pos), null);
            }

        }

        // Remove all null values from the array list and pass it to content provider
        while (items.remove(null)) ;
        updateTheContentProvider(items, null);
        getActivity().finish();
    }

    // Delete the whole list and the list items from content provider
    private int deleteList() {

        int rowId = getActivity().getContentResolver().delete(
                ListContract.ListEntry.CONTENT_URI,

                ListContract.ListEntry.DATE + " =? ",
                new String[]{Long.toString(mListDate)}
        );

        getActivity().getContentResolver().delete(
                ListContract.ListItemsEntry.CONTENT_URI,

                ListContract.ListItemsEntry.DATE + " =? ",
                new String[]{Long.toString(mListDate)}
        );
        getActivity().finish();
        return rowId;
    }

    private void insertNewList(Context ctx) {
        ContentProviderUtils utils = new ContentProviderUtils(getActivity());
        utils.insertListAndListItem(mListName, updateListItems(), mIsListDefault, System.currentTimeMillis());
    }


    // This method sets the action bar on edit
    public void setActionbarOnEdit() {
        //  when clicking edit button only done item should be visible
        Log.i(Constants.LOG_TAG," in action bar edit ");
        if (mActionDelete == null || mActionDone == null || mActionEdit == null)
            return;
        mActionDone.setVisible(true);
        mActionDelete.setVisible(false);
        mActionEdit.setVisible(false);
    }

    // This method sets the action bar on delete
    public void setActionbarOnDelete() {
        //  when clicking edit button only done item should be visible
        if (mActionDelete == null || mActionDone == null || mActionEdit == null)
            return;
        mActionDone.setVisible(false);
        mActionDelete.setVisible(true);
        mActionEdit.setVisible(false);
    }

    public void resetTheActionBar() {
        if (mActionDelete == null || mActionDone == null || mActionEdit == null)
            return;
        mActionEdit.setVisible(true);
        mActionDelete.setVisible(true);
        mActionDone.setVisible(false);
    }

    private ArrayList<String> updateListItems() {
        ArrayList<String> updateList = new ArrayList<>();

        for (int i = 0; i < mDisplayListItems.getAdapter().getCount(); i++) {
            View listItem = mDisplayListItems.getChildAt(i);
            if (listItem == null)
                return null;
            EditText listItemDetails = (EditText) listItem.findViewById(R.id.list_item_details);
            updateList.add(listItemDetails.getText().toString());
        }
        updateList.add(mAddNewItemEditlist.getText().toString());
        return updateList;
    }

    @Override
    public void onDestroyView() {
        // In case user presses the back button and list item has not been updated
        // then update the list now
        super.onDestroyView();

    }
}

