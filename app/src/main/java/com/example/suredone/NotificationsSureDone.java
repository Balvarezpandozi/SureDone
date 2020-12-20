package com.example.suredone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.suredone.calendar.calendar;
import com.example.suredone.hotlist.hotlist;
import com.example.suredone.inbox.inbox;


public class NotificationsSureDone {

    NotificationManager manager;
    //INBOX

    public void createInboxChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel inboxChannel = new NotificationChannel("inboxChannel", "inboxNotification", NotificationManager.IMPORTANCE_DEFAULT);
            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(inboxChannel);
        }
    }

    public NotificationCompat.Builder createInboxNotification(Context context){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String content;
        if (dataBaseHelper.isInboxEmpty()){
            content = "Remember to add your chores to the INBOX!!";
        }else {
            content = "Let's process the tasks in your INBOX!!";
        }

        Intent resultIntent = new Intent(context, inbox.class);
        PendingIntent inboxIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "inboxChannel");
        builder.setContentTitle("Inbox");
        builder.setContentText(content);
        builder.setContentIntent(inboxIntent);
        builder.setSmallIcon(R.drawable.icon_notification);
        builder.setAutoCancel(true);
        return builder;
    }

    public void showInboxNotification(Context context, NotificationCompat.Builder builder){
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());
    }


    //HOTLIST

    public void createHotlistChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel hotlistChannel = new NotificationChannel("hotlistChannel", "hotlistNotification", NotificationManager.IMPORTANCE_DEFAULT);
            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(hotlistChannel);
        }
    }

    public NotificationCompat.Builder createHotlistNotification(Context context){

        Intent resultIntent = new Intent(context, hotlist.class);
        PendingIntent hotlistIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hotlistChannel");
        builder.setContentTitle("Hotlist");
        builder.setContentIntent(hotlistIntent);
        builder.setContentText("Hey! you got stuff to do in the Hotlist...");
        builder.setSmallIcon(R.drawable.icon_notification);
        builder.setAutoCancel(true);
        return builder;
    }

    public void showHotlistNotification(Context context, NotificationCompat.Builder builder){
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(2, builder.build());
    }


    //CALENDAR
    public void createCalendarChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel calendarChannel = new NotificationChannel("calendarChannel", "calendarNotification", NotificationManager.IMPORTANCE_DEFAULT);
            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(calendarChannel);
        }
    }

    public NotificationCompat.Builder createCalendarNotification(Context context){
        Intent resultIntent = new Intent(context, calendar.class);
        PendingIntent calendarIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "calendarChannel");
        builder.setContentIntent(calendarIntent);
        builder.setContentTitle("Calendar");
        builder.setContentText("You have events for today, DON'T FORGET!");
        builder.setSmallIcon(R.drawable.icon_notification);
        builder.setAutoCancel(true);
        return builder;
    }

    public void showCalendarNotification(Context context, NotificationCompat.Builder builder){
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(3,builder.build());
    }
}
