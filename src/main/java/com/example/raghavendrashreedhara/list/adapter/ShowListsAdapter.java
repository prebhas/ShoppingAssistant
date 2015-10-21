package com.example.raghavendrashreedhara.list.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.raghavendrashreedhara.list.R;
import com.example.raghavendrashreedhara.list.utility.Constants;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import com.etsy.android.grid.util.DynamicHeightTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by raghavendrashreedhara on 5/16/15.
 */
public class ShowListsAdapter extends CursorAdapter {

    View mItemView;
    private ArrayList<ArrayList<String>> mListItems;
    private Queue<String> mListTitle;
    private int mSize;

    public ShowListsAdapter(Context context, Cursor c, int flags) {
        super(context, c);

    }


    public void setListTitle(ArrayDeque<String> list) {
        mListTitle = list;
        mSize = mListTitle.size();
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Log.i(Constants.LOG_TAG, "in new view ");
        View v = LayoutInflater.from(context).inflate(R.layout.show_list_adapter, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        //Log.i(Constants.LOG_TAG, "in bind view " + cursor.getColumnCount());
        if (convertView != null) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
             ArrayList<String> finalOutputString = new Gson().fromJson(cursor.getString(1), type);
            Log.d(Constants.LOG_TAG," in bind view "+finalOutputString);
            if (holder != null) {
                if (mListTitle != null && !mListTitle.isEmpty()) {
                    holder.title.setText(mListTitle.peek());
                    mListTitle.remove();
                }

                if (finalOutputString != null && !finalOutputString.isEmpty()) {
                    if (finalOutputString.size() >= 1) {
                        holder.listItem1.setText(finalOutputString.get(0));
                       // Log.i(Constants.LOG_TAG, "in bind view pos 1 " + finalOutputString.get(0));
                    } else
                        holder.listItem1.setText("");
                    if (finalOutputString.size() >= 2) {
                        holder.listItem2.setText(finalOutputString.get(1));
                       // Log.i(Constants.LOG_TAG, "in bind view pos 2 " + finalOutputString.get(1));

                    } else
                        holder.listItem2.setText("");
                    if (finalOutputString.size() >= 3) {

                       // Log.i(Constants.LOG_TAG, "in bind view pos 3 " + finalOutputString.get(2));
                        holder.listItem3.setText(finalOutputString.get(2));
                    } else
                        holder.listItem3.setText("");

                }
            } else
                return;


        }
    }


}


/**
 * Cache of the children views for a forecast list item.
 */
class ViewHolder {
    //public final ImageView iconView;
    public final TextView title;
    public final DynamicHeightTextView listItem1;
    public final DynamicHeightTextView listItem2;
    public final DynamicHeightTextView listItem3;


    public ViewHolder(View view) {

        listItem1 = (DynamicHeightTextView) view.findViewById(R.id.list_item_name_1);
        listItem2 = (DynamicHeightTextView) view.findViewById(R.id.list_item_name_2);
        listItem3 = (DynamicHeightTextView) view.findViewById(R.id.list_item_name_3);
        title = (TextView) view.findViewById(R.id.list_name);


    }


}




