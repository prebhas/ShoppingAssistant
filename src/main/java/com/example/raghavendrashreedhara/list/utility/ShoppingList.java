package com.example.raghavendrashreedhara.list.utility;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Parcelable class to send the shopping list.
 */
public class ShoppingList implements Parcelable {


    private String listname;
    private ArrayList<String> listItems;
    private long listDate;
    private long reminderDate;
    private boolean isListDefault;
    private int sectionNumber;

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }


    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public ArrayList<String> getListItems() {
        return listItems;
    }

    public void setListItems(ArrayList<String> listItems) {
        this.listItems = listItems;
    }

    public long getListDate() {
        return listDate;
    }

    public void setListDate(long listDate) {
        this.listDate = listDate;
    }

    public long getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(long reminderDate) {
        this.reminderDate = reminderDate;
    }


    public boolean getisListDefault() {
        return isListDefault;
    }

    public void setIsListDefault(boolean isListDefault) {
        this.isListDefault = isListDefault;
    }


    public static final Parcelable.Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
        public ShoppingList createFromParcel(Parcel source) {
            ShoppingList list = new ShoppingList();
            list.listname = source.readString();
            list.listItems = (ArrayList<String>) source.readSerializable();
            list.reminderDate = source.readLong();
            list.listDate = source.readLong();
            list.isListDefault = source.readByte() != 0;
            return list;
        }

        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(listname);
        parcel.writeSerializable(listItems);
        parcel.writeLong(reminderDate);
        parcel.writeLong(listDate);
        parcel.writeByte((byte) (isListDefault ? 1 : 0));
    }

    @Override
    public String toString() {
        return listname + " - " + listDate + " - " + listItems;
    }
}


