package com.example.suredone.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.suredone.DataBaseHelper;
import com.example.suredone.R;
import com.example.suredone.hotlist.hotlist;
import com.example.suredone.inbox.inbox;
import com.example.suredone.incubator.incubator;
import com.example.suredone.info.Info;
import com.example.suredone.ticklerFile.ticklerFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class calendar extends AppCompatActivity {

    ImageView generalMenuImageView;
    CalendarView eventsCalendarView;
    ListView eventsListView;

    DataBaseHelper dataBaseHelper;
    List<CalendarTask> calendarTasks;
    ArrayAdapter calendarAdapter;
    PopupMenu generalMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        generalMenuImageView = findViewById(R.id.generalMenuImageView);
        eventsCalendarView = findViewById(R.id.eventsCalendarView);
        eventsListView = findViewById(R.id.eventsListView);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        showCalendarTasksOnListView(dataBaseHelper);

        //ITEMS MENU SETUP
        registerForContextMenu(eventsListView);

        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CalendarTask calendarTask = (CalendarTask) parent.getItemAtPosition(position);
                setDateOnCalendarView(calendarTask.getDate());
            }
        });


    }

    //GENERAL MENU
    public void openGeneralMenu(View view) {
        generalMenu = new PopupMenu(getApplicationContext(), generalMenuImageView);
        generalMenu.inflate(R.menu.generalmenu);
        Menu menu = generalMenu.getMenu();
        menu.findItem(R.id.calendar).setEnabled(false);
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
                    case R.id.incubator:
                        Intent incubatorIntent = new Intent(getApplicationContext(), incubator.class);
                        startActivity(incubatorIntent);
                        return true;
                    case R.id.ticklerfile:
                        Intent ticklerFileIntent = new Intent(getApplicationContext(), ticklerFile.class);
                        startActivity(ticklerFileIntent);
                        return true;
                    case R.id.info:
                        Intent infoIntent = new Intent(getApplicationContext(), Info.class);
                        startActivity(infoIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        showCalendarTasksOnListView(dataBaseHelper);
    }

    // ITEMS MENU SETUP
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendartaskmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        CalendarTask calendarTask = (CalendarTask) eventsListView.getItemAtPosition(position);

        switch (item.getItemId()){
            case R.id.edit:
                Intent calendarFormIntent = new Intent(getApplicationContext(), calendarTaskForm.class);
                calendarFormIntent.putExtra("ID", calendarTask.getId());
                calendarFormIntent.putExtra("fromRequest", "fromCalendar");
                startActivity(calendarFormIntent);
                return true;
            case R.id.delete:
                dataBaseHelper.deleteCalendarTaskByID(calendarTask);
                showCalendarTasksOnListView(dataBaseHelper);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //Show tasks on ListView
    private void showCalendarTasksOnListView(DataBaseHelper dataBaseHelper1) {
        calendarAdapter = new CalendarCustomArrayAdapter(calendar.this, (ArrayList<CalendarTask>) dataBaseHelper1.getAllCalendarTasks());
        eventsListView.setAdapter(calendarAdapter);
    }

    public void deletePastEvents(View view){
        calendarTasks = dataBaseHelper.getAllCalendarTasks();
        for (int i = 0; i<calendarTasks.size(); i++) {
            String date = calendarTasks.get(i).getDate();
            Calendar now = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/YYYY");
            String nowDate = formatter.format(now.getTime());
            Log.i("date", date + " current: " + nowDate);
            String[] taskDate = date.split("/");
            String[] currentDate = nowDate.split("/");

            if (Integer.parseInt(taskDate[2]) <= Integer.parseInt(currentDate[2])) {
                if (Integer.parseInt(taskDate[2]) < Integer.parseInt(currentDate[2])) {
                    dataBaseHelper.deleteCalendarTaskByID(calendarTasks.get(i));
                } else if (Integer.parseInt(taskDate[2]) == Integer.parseInt(currentDate[2])) {
                    if (Integer.parseInt(taskDate[0]) <= Integer.parseInt(currentDate[0])) {
                        if (Integer.parseInt(taskDate[0]) < Integer.parseInt(currentDate[0])) {
                            dataBaseHelper.deleteCalendarTaskByID(calendarTasks.get(i));
                        } else if (Integer.parseInt(taskDate[0]) == Integer.parseInt(currentDate[0])) {
                            if (Integer.parseInt(taskDate[1]) <= Integer.parseInt(currentDate[1])) {
                                dataBaseHelper.deleteCalendarTaskByID(calendarTasks.get(i));
                            }
                        }
                    }
                }
            }
        }
        showCalendarTasksOnListView(dataBaseHelper);
    }

    public void setDateOnCalendarView(String dateToSet){
        String parts[] = dateToSet.split("/");
        Calendar calendar = Calendar.getInstance();

        int month = Integer.parseInt(parts[0]) - 1;
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long milliTime = calendar.getTimeInMillis();
        eventsCalendarView.setDate (milliTime, true, true);
    }
}