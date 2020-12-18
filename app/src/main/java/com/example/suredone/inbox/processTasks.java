package com.example.suredone.inbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.suredone.DataBaseHelper;
import com.example.suredone.R;
import com.example.suredone.calendar.calendarTaskForm;
import com.example.suredone.hotlist.hotlistTaskForm;
import com.example.suredone.incubator.incubatorTaskForm;
import com.example.suredone.ticklerFile.ticklerFileForm;

public class processTasks extends AppCompatActivity {

    TextView taskTitle;
    int taskID;
    InboxTask inboxTask;
    DataBaseHelper dataBaseHelper;
    String fromRequest = "fromInbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_tasks);

        taskTitle = findViewById(R.id.taskTItle);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        Intent intent = getIntent();
        taskID = intent.getIntExtra("inboxID", -1);

        inboxTask = dataBaseHelper.getInboxTaskByID(taskID);
        taskTitle.setText(inboxTask.getTaskDescription());

        taskTitle.setMovementMethod(new ScrollingMovementMethod());
    }

    public void hotlistProcess(View view){
        Intent hotlistFormIntent = new Intent(getApplicationContext(), hotlistTaskForm.class);
        hotlistFormIntent.putExtra("ID", taskID);
        hotlistFormIntent.putExtra("fromRequest", fromRequest);
        startActivity(hotlistFormIntent);
        finish();
    }

    public void ticklerFileProcess(View view){
        Intent ticklerFileFormIntent = new Intent(getApplicationContext(), ticklerFileForm.class);
        ticklerFileFormIntent.putExtra("ID", taskID);
        ticklerFileFormIntent.putExtra("fromRequest", fromRequest);
        startActivity(ticklerFileFormIntent);
        finish();
    }

    public void calendarProcess(View view){
        Intent calendarFormIntent = new Intent(getApplicationContext(), calendarTaskForm.class);
        calendarFormIntent.putExtra("ID", taskID);
        calendarFormIntent.putExtra("fromRequest", fromRequest);
        startActivity(calendarFormIntent);
        finish();
    }

    public void incubatorProcess(View view){
        Intent incubatorFormIntent = new Intent(getApplicationContext(), incubatorTaskForm.class);
        incubatorFormIntent.putExtra("ID", taskID);
        incubatorFormIntent.putExtra("fromRequest", fromRequest);
        startActivity(incubatorFormIntent);
        finish();
    }

    public void skipProcess(View view){
        finish();
    }
}
