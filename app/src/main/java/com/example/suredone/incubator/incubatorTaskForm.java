package com.example.suredone.incubator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.suredone.DataBaseHelper;
import com.example.suredone.inbox.InboxTask;
import com.example.suredone.R;

public class incubatorTaskForm extends AppCompatActivity {

    EditText titleEditText;
    Button saveIncubatorTaskButton;

    int taskID;
    String fromRequest;
    String title;
    DataBaseHelper dataBaseHelper;
    IncubatorTask incubatorTask;
    InboxTask inboxTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incubator_task_form);

        titleEditText = findViewById(R.id.titleEditText);
        saveIncubatorTaskButton = findViewById(R.id.saveIncubatorTaskButton);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        Intent intent = getIntent();
        taskID = intent.getIntExtra("ID", -1);
        fromRequest = intent.getStringExtra("fromRequest");

        if (fromRequest.equals("fromInbox")){
            inboxTask = dataBaseHelper.getInboxTaskByID(taskID);
            titleEditText.setText(inboxTask.getTaskDescription());

        }else if (fromRequest.equals("fromIncubator")){
            incubatorTask = dataBaseHelper.getIncubatorTaskByID(taskID);
            titleEditText.setText(incubatorTask.getDescription());
        }
    }

    public void saveIncubatorTask(View view){
        title = titleEditText.getText().toString();
        incubatorTask = new IncubatorTask(taskID , title);

        if(fromRequest.equals("fromInbox")){
            dataBaseHelper.deleteInboxTaskByID(inboxTask);
            dataBaseHelper.addIncubatorTask(incubatorTask);
            finish();
        }else if (fromRequest.equals("fromIncubator")){
            dataBaseHelper.updateIncubatorTaskByID(incubatorTask);
            finish();
        }
    }
}
