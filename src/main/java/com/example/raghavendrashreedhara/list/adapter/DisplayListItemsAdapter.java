package com.example.raghavendrashreedhara.list.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.raghavendrashreedhara.list.R;

import com.example.raghavendrashreedhara.list.fragment.ListItemsDisplayActivityFragment;
import com.example.raghavendrashreedhara.list.utility.Constants;
import com.example.raghavendrashreedhara.list.utility.UpdateList;


import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by root on 8/27/15.
 */
public class DisplayListItemsAdapter extends ArrayAdapter<String> {
    private int mId;
    private Context mContext;
    private ArrayList<String> mDataset;
    private EditText mChangedEditText;
    private HashSet<Integer> mClickedPosition = new HashSet<>();
    private ListItemsDisplayActivityFragment mFrag;
    private boolean mIsListTimeEdited;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DisplayListItemsAdapter(Context ctx, ArrayList<String> list, ListItemsDisplayActivityFragment frag) {
        super(ctx, -1, list);
        // Log.i(Constants.LOG_TAG, "in display list items " + myDataset.size());
        mContext = ctx;
        mDataset = list;
        mFrag = frag;
        UpdateList.setList(list);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder {
        // each data item is just a string in this case
        public EditText mEditText;
        private CheckBox mCheckBox;
}



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.display_list_item_adapter,null,false);
            holder.mEditText = (EditText) convertView.findViewById(R.id.list_item_details);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.list_item_details_checkbox);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (mDataset != null) {
            mChangedEditText = holder.mEditText;

            holder.mEditText.setText(mDataset.get(position));
            /**
             * This method gets the text from the edit text when user has finished editing the
             * items and updates the list with the updates list item
             */
            holder.mEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFrag.setActionbarOnEdit();
                }
            });
        }
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                android.util.Log.d(Constants.LOG_TAG, "in on checked change listener " + position + isChecked);
                if (isChecked) {
                    mClickedPosition.add(position);
                    // set the action bar only once
                    mFrag.setActionbarOnDelete();
                } else {
                   // resetting the action bar since none of the items are clicked
                    mFrag.resetTheActionBar();
                }

            }
        });
        convertView.setTag(holder);
        return convertView;
    }



    public HashSet<Integer> getDeleteItemsIndex() {
        Log.i(Constants.LOG_TAG," in delete items index"+mClickedPosition.size());
        for(String listItem:mDataset)
            Log.i(Constants.LOG_TAG, " items in adapter are " + listItem);
        return mClickedPosition;
    }


}

