package com.example.suredone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    NotificationsSureDone notifications = new NotificationsSureDone();
    DataBaseHelper dataBaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        dataBaseHelper = new DataBaseHelper(context);

        //Inbox Notification
        notifications.createInboxChannel(context);
        notifications.showInboxNotification(context, notifications.createInboxNotification(context));

        //Hotlist Notification
        if (!dataBaseHelper.isHotlistEmpty()){
            notifications.createHotlistChannel(context);
            notifications.showHotlistNotification(context, notifications.createHotlistNotification(context));
        }

        //Calendar Notification
        if(dataBaseHelper.isCalendarEventToday()){
            notifications.createCalendarChannel(context);
            notifications.showCalendarNotification(context, notifications.createCalendarNotification(context));
        }
    }
}
