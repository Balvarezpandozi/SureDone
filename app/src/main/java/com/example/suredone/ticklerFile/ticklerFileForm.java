package com.example.suredone.ticklerFile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.suredone.DataBaseHelper;
import com.example.suredone.inbox.InboxTask;
import com.example.suredone.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ticklerFileForm extends AppCompatActivity {

    EditText titleEditText;
    Button saveTicklerFileTaskButton;
    CalendarView activeDateView;
    Calendar calendar;
    DataBaseHelper dataBaseHelper;

    InboxTask inboxTask;
    TicklerFileTask ticklerFileTask;

    String date;
    String fromRequest;
    int taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickler_file_form);

        titleEditText = findViewById(R.id.titleEditText);
        saveTicklerFileTaskButton = findViewById(R.id.saveTicklerFileTaskButton);
        activeDateView = findViewById(R.id.activeDateView);
        calendar = Calendar.getInstance();

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        Intent intent = getIntent();
        fromRequest = intent.getStringExtra("fromRequest");
        taskID = intent.getIntExtra("ID", -1);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/YYYY");
        date = formatter.format(calendar.getTime());

        if (fromRequest.equals("fromInbox")){
            inboxTask = dataBaseHelper.getInboxTaskByID(taskID);
            titleEditText.setText(inboxTask.getTaskDescription());
            // Calendar sets on phone's date for default.
        }else if(fromRequest.equals("fromTicklerFile")){
            ticklerFileTask = dataBaseHelper.getTicklerFileTaskByID(taskID);
            titleEditText.setText(ticklerFileTask.getTitle());
            setDateOnCalendarView(ticklerFileTask.getActiveDate());
        }

        activeDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = (month + 1) + "/" + dayOfMonth + "/" + year;
                Log.i("date", date);
            }
        });
    }

    public void saveTicklerFileTask(View view) {
        String title = titleEditText.getText().toString();
        TicklerFileTask ticklerFileTask = new TicklerFileTask(taskID, title, date);

        if (fromRequest.equals("fromInbox")) {
            dataBaseHelper.deleteInboxTaskByID(inboxTask);
            dataBaseHelper.addTicklerFileTask(ticklerFileTask);
            finish();
        } else if (fromRequest.equals("fromTicklerFile")) {
            dataBaseHelper.updateTicklerFileTaskByID(ticklerFileTask);
            finish();
        }
    }

    public void setDateOnCalendarView(String dateToSet){
        String parts[] = dateToSet.split("/");

        int month = Integer.parseInt(parts[0]) - 1;
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long milliTime = calendar.getTimeInMillis();
        activeDateView.setDate (milliTime, true, true);
    }
}
