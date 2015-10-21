package com.example.raghavendrashreedhara.list.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.example.raghavendrashreedhara.list.R;
import com.example.raghavendrashreedhara.list.activity.ListItemsDisplayActivity;
import com.example.raghavendrashreedhara.list.activity.MainActivity;
import com.example.raghavendrashreedhara.list.adapter.ShowListsAdapter;
import com.example.raghavendrashreedhara.list.loader.ListTypeLoader;
import com.example.raghavendrashreedhara.list.loader.ListsLoader;
import com.example.raghavendrashreedhara.list.utility.Constants;
import com.example.raghavendrashreedhara.list.utility.ShoppingList;
import com.example.raghavendrashreedhara.list.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Main list fragment shows the lists in staggered view .
 */
public class MainListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    public StaggeredGridView mListStaggeredView;
    private ShowListsAdapter mStaggeredViewAdapter;


    // Both list and lis items cursor
    private Cursor mListCursor;
    private Cursor mListItemsCursor;
    // This is the list name queue .
    private ArrayList<String> mListName = new ArrayList<>();

    // This is the dates queue which is the primary key of list and list items
    private ArrayList<Long> mDatesList = new ArrayList<>();
    //This is the alarm time set
    private ArrayList<Long> mAlarmTime = new ArrayList<>();
    private long mListDate;
    private String mSingleListName;
    private boolean mIsDefault = false;


    public MainListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainListFragment newInstance(int sectionNumber, long date, String list) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        ShoppingList slist = new ShoppingList();
        slist.setSectionNumber(sectionNumber);
        slist.setListDate(date);
        slist.setListname(list);
        args.putParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST, slist);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListStaggeredView = (StaggeredGridView) rootView.findViewById(R.id.staggered_view);

        mListStaggeredView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        long time = 0;
        if (getArguments() != null) {
            ShoppingList list =getArguments().getParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST);
            int id =-1;int date =-1;
            if(list!=null) {
                id = list.getSectionNumber();
                mSingleListName = list.getListname();
            }


            time = Utils.getTheLongUpdatedTime(getActivity());
            if (time != 0) {
                mListDate = time;
                Utils.setTimeintoSharedPreferences(getActivity(), 0);
            } else {
                mListDate = list.getListDate();
            }


            ((MainActivity) getActivity()).setActionBarTitle(mSingleListName);
            switch (id) {
                // Display all lists
                case 1:
                    calltheAllListsLoader(id);
                    break;
                case 2:
                    //set the boolean that default list is being called
                    mIsDefault = true;
                    // Display the default lists
                    callTheDefaultListsLoader(id);
                    break;
                default:
                    callTheListTypeLoader(mListDate);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mListCursor = null;
        mListItemsCursor = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        int sectionNumber =0;
        if(getArguments() != null){
            ShoppingList list = getArguments().getParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST);
            sectionNumber = list.getSectionNumber();
        }
        ((MainActivity) activity).onSectionAttached(
               sectionNumber);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.LIST_LOADER:
                return new ListsLoader(getActivity(), Constants.LIST_LOADER);
            case Constants.LIST_ITEMS_LOADER:
                // Returns list items
                return new ListsLoader(getActivity(), Constants.LIST_ITEMS_LOADER);
            case Constants.DEFAULT_LIST_LOADER:
                // Returns the list items
                return new ListTypeLoader(getActivity(), Constants.DEFAULT_LIST_LOADER, null);
            case Constants.DEFAULT_LIST_ITEMS_LOADER:
                // Returns list items
                ArrayList<String> dates = args.getStringArrayList(Constants.ARRAY_LIST_DATE);
                return new ListTypeLoader(getActivity(), Constants.DEFAULT_LIST_ITEMS_LOADER,
                        dates.toArray(new String[dates.size()]));
            case Constants.LIST_TYPE_LIST_ITEMS_LOADER:
                // returns the list items corresponding to the list names
                return new ListTypeLoader(getActivity(), Constants.LIST_TYPE_LIST_ITEMS_LOADER, new String[]{Long.toString(mListDate)});
        }

        return null;
    }


    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        Log.i(Constants.LOG_TAG," on load finished "+data.getCount());
        if(data == null)
            return;
        int id = loader.getId();
        switch (id) {
            //This case is called when all the list details have to displayed
            case Constants.LIST_LOADER:
                mListCursor = data;
                break;
            // This is called when user clicks "default" in navigation fragment
            case Constants.DEFAULT_LIST_LOADER:
                mListCursor = data;
                ArrayList<Long> dates = Utils.returnListDates(mListCursor,Constants.LIST_DATE_INDEX);
                ArrayList<String> newList = new ArrayList<>(dates.size());
                for (long myInt : dates) {
                    newList.add(String.valueOf(myInt));
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(Constants.ARRAY_LIST_DATE, newList);
                // call the
                getActivity().getSupportLoaderManager().initLoader(Constants.DEFAULT_LIST_ITEMS_LOADER, bundle, this).forceLoad();
                break;
            // This is called when user clicks on different type of list names
            case Constants.LIST_TYPE_LIST_ITEMS_LOADER:
                mListItemsCursor = data;
                populatetheListAndListItemsData();
                return;
            // This populates the list items
            case Constants.LIST_ITEMS_LOADER:
            case Constants.DEFAULT_LIST_ITEMS_LOADER:
            default:
                mListItemsCursor = data;
                break;
        }
        if (mListItemsCursor != null && mListCursor != null)
            populatetheListAndListItemsData();

    }

    private void populatetheListAndListItemsData() {

        if (mListName != null)
            mListName.clear();
        if (mDatesList != null)
            mDatesList.clear();
        // we already know the list name so set that to adapter
        if (mListCursor == null && mListDate != 0) {
            mListName.add(mSingleListName);
            mDatesList.add(mListDate);
        } else {
            // we already know the list name so set that to adapter
            mListName = Utils.returnListNames(mListCursor);
            mDatesList = Utils.returnListDates(mListCursor, Constants.LIST_DATE_INDEX);
            mAlarmTime = Utils.returnListDates(mListCursor, Constants.ALARM_DATE_INDEX);

        }
        if(mListItemsCursor !=null) {
            setTheListAdapter();
            if (mStaggeredViewAdapter != null) {
                reloadAdapter();
            }
        }
    }

    private void setTheListAdapter() {
        mStaggeredViewAdapter = new ShowListsAdapter(getActivity(), mListItemsCursor, 0);
        mListStaggeredView.setAdapter(mStaggeredViewAdapter);
        reloadAdapter();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.abandon();
    }

    private void calltheAllListsLoader(int id) {
        Bundle bundle = new Bundle();
        ShoppingList list = new ShoppingList();
        list.setSectionNumber(id);
        bundle.putParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST, list);
        // Start both the list and list items loader
        getActivity().getSupportLoaderManager().initLoader(Constants.LIST_ITEMS_LOADER, bundle, this).forceLoad();
        getActivity().getSupportLoaderManager().initLoader(Constants.LIST_LOADER, bundle, this).forceLoad();
    }

    private void callTheDefaultListsLoader(int id) {
        // Start both the list and list items loader
        Bundle bundle = new Bundle();
        ShoppingList list = new ShoppingList();
        list.setSectionNumber(id);
        bundle.putParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST, list);
        getActivity().getSupportLoaderManager().initLoader(Constants.DEFAULT_LIST_LOADER, bundle, this).forceLoad();
    }

    private void callTheListTypeLoader(long date) {
        Bundle bundle = new Bundle();
        ShoppingList list = new ShoppingList();
        list.setListDate(date);
        bundle.putParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST, list);
        getActivity().getSupportLoaderManager().initLoader(Constants.LIST_TYPE_LIST_ITEMS_LOADER, bundle, this).forceLoad();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Here id returns position of the list

        if (mDatesList == null || mDatesList.isEmpty())
            return;
        // Refresh the list name and the list items in the adapter
        reloadAdapter();

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        if (mListItemsCursor != null) {
            mListItemsCursor.moveToPosition(position);
            if (mListItemsCursor.getCount() > 0) {
                ArrayList<String> finalOutputString = new Gson().fromJson(mListItemsCursor.getString(1), type);
                Intent intent = new Intent(getActivity(), ListItemsDisplayActivity.class);
                ShoppingList list = new ShoppingList();
                list.setListItems(finalOutputString);
                list.setListname(mListName.get(position));
                list.setListDate(mDatesList.get(position));
                list.setIsListDefault(mIsDefault);
                intent.putExtra(Constants.GET_PARCELABLE_SHOPPING_LIST,list);
                getActivity().startActivity(intent);
            }
        } else
            return;
    }

    private void reloadAdapter() {
        mStaggeredViewAdapter.notifyDataSetInvalidated();
        mStaggeredViewAdapter.notifyDataSetChanged();
        if (mListName != null && !mListName.isEmpty()) {
            ArrayDeque<String> deque = new ArrayDeque(mListName);
            mStaggeredViewAdapter.setListTitle(deque);
        }

    }

}

