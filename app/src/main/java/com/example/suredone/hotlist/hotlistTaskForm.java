package com.example.suredone.hotlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.suredone.DataBaseHelper;
import com.example.suredone.inbox.InboxTask;
import com.example.suredone.R;


public class hotlistTaskForm extends AppCompatActivity {

    EditText titleEditText;
    EditText descriptionAndDetailsEditText;

    Button saveHotlistTaskButton;

    int taskID;
    String fromRequest;
    String title;
    String description;
    DataBaseHelper dataBaseHelper;
    HotlistTask hotlistTask;
    InboxTask inboxTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotlist_task_form);

        Intent intent = getIntent();
        taskID = intent.getIntExtra("ID", -1);
        fromRequest = intent.getStringExtra("fromRequest");
        titleEditText = findViewById(R.id.titleEditText);
        descriptionAndDetailsEditText = findViewById(R.id.descriptionAndDetailsEditText);
        saveHotlistTaskButton = findViewById(R.id.saveHotlistTaskButton);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        if (fromRequest.equals("fromInbox")){
            inboxTask = dataBaseHelper.getInboxTaskByID(taskID);
            descriptionAndDetailsEditText.setText(inboxTask.getTaskDescription());

        }else if (fromRequest.equals("hotlist")){
            hotlistTask = dataBaseHelper.getHotlistTaskByID(taskID);
            titleEditText.setText(hotlistTask.getTitle());
            descriptionAndDetailsEditText.setText(hotlistTask.getTaskDescription());
        }
    }

    public void saveHotlistTask(View view){
        title = titleEditText.getText().toString();
        description = descriptionAndDetailsEditText.getText().toString();
        hotlistTask = new HotlistTask(taskID , description, title, false);

        if(fromRequest.equals("fromInbox")){
            dataBaseHelper.deleteInboxTaskByID(inboxTask);
            dataBaseHelper.addHotlistTask(hotlistTask);
            finish();
        }else if (fromRequest.equals("hotlist")){
            boolean aja = dataBaseHelper.updateHotlistTaskByID(hotlistTask);
            Log.i("si o que", String.valueOf(aja));
            finish();
        }
    }
}
