package com.example.raghavendrashreedhara.list.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raghavendrashreedhara.list.R;
import com.example.raghavendrashreedhara.list.adapter.RecyclerAdapter;
import com.example.raghavendrashreedhara.list.utility.Constants;
import com.example.raghavendrashreedhara.list.utility.ShoppingList;

import java.util.ArrayList;

/**
 * Fragments displays the new item added by the user in add activity
 */
public class DisplayListItemsInAddFragment extends Fragment  {


    private RecyclerView mRecyclerview ;
    private RecyclerAdapter mRecyclerAdapter;
    private static ArrayList<String> mListItem = new ArrayList<String>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getArguments()!=null) {
            ShoppingList list = getArguments().getParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST);
            mListItem = list.getListItems();

        }

        mRecyclerAdapter = new RecyclerAdapter(mListItem);
        View rootView = inflater.inflate(R.layout.display_list_item, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        mRecyclerview = (RecyclerView) rootView.findViewById(R.id.add_list_recycler_view);
        //mListView.setAdapter(mListAdapter);
        // use a linear layout manager

        mRecyclerview.setLayoutManager( new LinearLayoutManager(getActivity()));

        // specify an adapter (see also next example)
        mRecyclerview.setAdapter(mRecyclerAdapter);
        return rootView;
    }

    /*
    This method refreshes the adapter to show the new data
     */
    public void refresh(String data) {

        mRecyclerAdapter.notifyDataSetChanged();
    }
}
