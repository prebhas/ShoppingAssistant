package com.example.raghavendrashreedhara.list.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.raghavendrashreedhara.list.service.NotificationService;
import com.example.raghavendrashreedhara.list.utility.Constants;

/**
 * Created by root on 9/8/15.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Show the notification on receiving the alarm intent
        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtra(Constants.GET_PARCELABLE_SHOPPING_LIST, intent.getParcelableExtra(Constants.GET_PARCELABLE_SHOPPING_LIST));
        context.startService(serviceIntent);

    }


}
