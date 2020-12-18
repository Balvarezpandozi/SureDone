package com.example.suredone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.suredone.calendar.CalendarTask;

import java.util.List;

public class SureDoneBroadcastReceiver extends BroadcastReceiver {

    DataBaseHelper dataBaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        dataBaseHelper = new DataBaseHelper(context);

        if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction())){
            List<CalendarTask> calendarTasks = dataBaseHelper.getAllCalendarTasks();
            for (int i = 0; i<calendarTasks.size(); i++){
                Log.i("broadcastReceiver", "pues funciono");
            }
        }

    }
}
