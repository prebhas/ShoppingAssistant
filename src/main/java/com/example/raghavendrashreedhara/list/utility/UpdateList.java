package com.example.raghavendrashreedhara.list.utility;

import java.util.ArrayList;

/**
 * Used to update the list in the displayitems adapter .
 */
public  class UpdateList {
    private static ArrayList<String> mList;

    public static ArrayList<String> getList() {
        return mList;
    }

    public static void setList( ArrayList<String> list) {
        mList = list;
    }
}
