package com.example.raghavendrashreedhara.list.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.example.raghavendrashreedhara.list.R;
import com.example.raghavendrashreedhara.list.fragment.ListItemsDisplayActivityFragment;
import com.example.raghavendrashreedhara.list.utility.Constants;

import java.util.ArrayList;

public class ListItemsDisplayActivity extends ActionBarActivity {
    private ListItemsDisplayActivityFragment mFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items_display);
        if (savedInstanceState == null && getIntent() != null) {
            mFrag = new ListItemsDisplayActivityFragment();
            Bundle bundle = new Bundle();
            // Our bundle consists of array list of items , the list name and list date
            bundle.putParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST,getIntent().getParcelableExtra(Constants.GET_PARCELABLE_SHOPPING_LIST));
            mFrag.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment, mFrag,Constants.DISPLAY_LIST_ITEMS_FRAGMENT);
            ft.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void upDateList(ArrayList<String> list) {
        mFrag.updateList();

    }

    // This method sets the action bar title as well as the back indictor in the action bar
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
    }
}
