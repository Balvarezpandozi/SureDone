package com.example.suredone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.suredone.inbox.inbox;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    Handler handler;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View iconImageView = findViewById(R.id.iconImageView);
        handler = new Handler();

        //Set Daily Notification for the Inbox
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar startTime = Calendar.getInstance();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        iconImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iconImageView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_out));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), inbox.class);
                        startActivity(intent);
                        finish();
                    }
                }, 500);
            }
        }, 1000);
    }
}
