package com.example.suredone.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.example.suredone.R;
import com.example.suredone.calendar.calendar;
import com.example.suredone.hotlist.hotlist;
import com.example.suredone.inbox.inbox;
import com.example.suredone.incubator.incubator;
import com.example.suredone.ticklerFile.ticklerFile;

public class Info extends AppCompatActivity {

    ImageView generalMenuImageView;
    PopupMenu generalMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        generalMenuImageView = findViewById(R.id.generalMenuImageView);
    }

    public void openGeneralMenu(View view){
        generalMenu = new PopupMenu(getApplicationContext(), generalMenuImageView);
        generalMenu.inflate(R.menu.generalmenu);
        Menu menu = generalMenu.getMenu();
        menu.findItem(R.id.info).setEnabled(false);
        generalMenu.show();
        generalMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inbox:
                        Intent inboxIntent = new Intent(getApplicationContext(), inbox.class);
                        startActivity(inboxIntent);
                        return true;
                    case R.id.hotlist:
                        Intent hotlistIntent = new Intent(getApplicationContext(), hotlist.class);
                        startActivity(hotlistIntent);
                        return true;
                    case R.id.calendar:
                        Intent calendarIntent = new Intent(getApplicationContext(), calendar.class);
                        startActivity(calendarIntent);
                        return true;
                    case R.id.incubator:
                        Intent incubatorIntent = new Intent(getApplicationContext(), incubator.class);
                        startActivity(incubatorIntent);
                        return true;
                    case R.id.ticklerfile:
                        Intent ticklerFileIntent = new Intent(getApplicationContext(), ticklerFile.class);
                        startActivity(ticklerFileIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}


