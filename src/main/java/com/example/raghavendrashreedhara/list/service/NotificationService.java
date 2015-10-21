package com.example.raghavendrashreedhara.list.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.example.raghavendrashreedhara.list.R;
import com.example.raghavendrashreedhara.list.activity.ListItemsDisplayActivity;
import com.example.raghavendrashreedhara.list.data.ListContract;
import com.example.raghavendrashreedhara.list.utility.Constants;
import com.example.raghavendrashreedhara.list.utility.ShoppingList;
import com.example.raghavendrashreedhara.list.utility.Utils;

import java.util.ArrayList;

/**
 * Service which posts the notification when user has set reminder to send the list
 */
public class NotificationService extends Service implements Loader.OnLoadCompleteListener<Cursor> {
    private CursorLoader mCursorLoader;
    private long mListDate;
    private String mListName;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            ShoppingList list = intent.getParcelableExtra(Constants.GET_PARCELABLE_SHOPPING_LIST);
            if(list!=null) {
                mListDate = list.getListDate();
                mListName = list.getListname();
            }

        }
        if (mListDate != 0L) {

            mCursorLoader = new CursorLoader(getApplicationContext(), ListContract.ListItemsEntry.CONTENT_URI,
                    new String[]{ListContract.ListItemsEntry.LIST_ITEMS},
                    ListContract.ListItemsEntry.DATE + " =? ",
                    new String[]{String.valueOf(mListDate)},
                    null);
            mCursorLoader.registerListener(Constants.NOTIFICATION_ID, this);
            mCursorLoader.startLoading();

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (mCursorLoader != null) {
            mCursorLoader.unregisterListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void postNotification( ArrayList<String> listitems) {
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        Intent resultIntent = new Intent(getApplicationContext(), ListItemsDisplayActivity.class);
        ShoppingList list = new ShoppingList();
        list.setListname(mListName);
        list.setListDate(mListDate);
        list.setListItems(listitems);
        resultIntent.putExtra(Constants.GET_PARCELABLE_SHOPPING_LIST,list);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.abc_btn_radio_material);
        drawable.setAlpha(255);
        String item1 = null;
        String item2 = null;
        String item3 = null;
        String summary = null;

        if (listitems != null && !listitems.isEmpty()) {
            if (listitems.size() >= 1)
                item1 = listitems.get(0);
            if (listitems.size() >= 2)
                item2 = listitems.get(1);
            if (listitems.size() >= 3)
                item3 = listitems.get(2);
            if (listitems.size() >= 4) {
                summary = "+ " + (listitems.size() - 3) + " more";
            }
        }
        // Using the supposrt library so that the notification is compatible across all devices
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(mListName)
                .setContentText(TextUtils.join(",", listitems))
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(item1)
                        .addLine(item2)
                        .addLine(item3)
                        .addLine(summary))
                .setSmallIcon(R.drawable.abc_btn_radio_material)
                .addAction(
                        R.drawable.abc_ic_menu_share_mtrl_alpha,
                        "Share",
                        PendingIntent.getActivity(getApplicationContext(), 0,
                                Utils.createShareListIntent(mListName, listitems), 0, null)).build();

        notification.contentIntent = resultPendingIntent;
         // Gets an instance of the NotificationManager service

        NotificationManager mNotifyMgr =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(Constants.NOTIFICATION_ID, notification);
    }


    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        ArrayList<String> listItemNames = Utils.returnListItems(data, 0);
        postNotification(listItemNames);
    }

}
