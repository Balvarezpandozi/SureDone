package com.example.suredone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class calendarTaskForm extends AppCompatActivity {

    EditText titleEditText;
    CalendarView activeDateView;
    TextView startTimeDisplayTextView;
    TextView endTimeDisplayTextView;
    Button saveCalendarTaskButton;

    DataBaseHelper dataBaseHelper;
    InboxTask inboxTask;
    CalendarTask calendarTask;
    Calendar calendar;

    String date;
    String fromRequest;
    int taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_task_form);

        titleEditText = findViewById(R.id.titleEditText);
        activeDateView = findViewById(R.id.activeDateView);
        startTimeDisplayTextView = findViewById(R.id.startTimeDisplayTextView);
        endTimeDisplayTextView = findViewById(R.id.endTimeDisplayTextView);
        saveCalendarTaskButton = findViewById(R.id.saveTaskOnCalendarButton);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        calendar = Calendar.getInstance();

        Intent intent = getIntent();
        fromRequest = intent.getStringExtra("fromRequest");
        taskID = intent.getIntExtra("ID", -1);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/YYYY");
        date = formatter.format(calendar.getTime());

        if (fromRequest.equals("fromInbox")){
            inboxTask = dataBaseHelper.getInboxTaskByID(taskID);
            titleEditText.setText(inboxTask.getTaskDescription());
        }else if(fromRequest.equals("fromCalendar")){
            calendarTask = dataBaseHelper.getCalendarTaskByID(taskID);
            titleEditText.setText(calendarTask.getTitle());
            setDateOnCalendarView(calendarTask.getDate());
            startTimeDisplayTextView.setText(calendarTask.getStartTime());
            endTimeDisplayTextView.setText(calendarTask.getEndTime());
        }

        activeDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = (month + 1) + "/" + dayOfMonth + "/" + year;
                Log.i("date", date);
            }
        });

    }

    public void openStartTimePicker(View view){
        TimePickerDialog timePickerDialog;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(calendarTaskForm.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTimeDisplayTextView.setText(hourOfDay + ":" + minute);
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Select Start Time");
        timePickerDialog.show();
    }

    public void openEndTimePicker(View view){
        TimePickerDialog timePickerDialog;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(calendarTaskForm.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTimeDisplayTextView.setText(hourOfDay + ":" + minute);
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Select End Time");
        timePickerDialog.show();
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

    public void saveCalendarTask(View view){
        String title = titleEditText.getText().toString();
        String startTime = startTimeDisplayTextView.getText().toString();
        String endTime = endTimeDisplayTextView.getText().toString();
        CalendarTask calendarTask = new CalendarTask(taskID, title, date, startTime, endTime);

        if (fromRequest.equals("fromInbox")) {
            dataBaseHelper.deleteInboxTaskByID(inboxTask);
            dataBaseHelper.addCalendarTask(calendarTask);
            finish();
        } else if (fromRequest.equals("fromCalendar")) {
            dataBaseHelper.updateCalendarTaskByID(calendarTask);
            finish();
        }
    }
}
