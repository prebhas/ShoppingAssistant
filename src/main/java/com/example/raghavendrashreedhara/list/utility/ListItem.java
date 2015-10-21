package com.example.raghavendrashreedhara.list.utility;

import java.util.ArrayList;

/**
 * List class with setters and getters
 */
public class ListItem {

    private static ArrayList<String> mListItemDescription = new ArrayList<String>();
    private String mListTitle;

    public  ArrayList<String> getItemNames() {
        return mItemNames;
    }

    public  void setItemNames(ArrayList<String> mItemNames) {
        ListItem.mItemNames = mItemNames;
    }

    private static ArrayList<String> mItemNames = new ArrayList<String>();

    public final static int LIST_TITLE_INDEX = 0;
    public final static int LIST_ITEM_INDEX = 0;


    public String getListTitle() {
        return mListTitle;
    }

    public void setListTitle(String listTitle) {
        this.mListTitle = listTitle;
    }



    public static ArrayList<String> getListItemDescription() {
        return mListItemDescription;
    }

    public static void setListItemDescription(ArrayList<String> listItemDescription) {
        mListItemDescription = listItemDescription;
    }




}
