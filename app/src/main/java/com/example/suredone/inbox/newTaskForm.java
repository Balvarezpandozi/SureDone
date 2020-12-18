package com.example.suredone.inbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.suredone.DataBaseHelper;
import com.example.suredone.R;

public class newTaskForm extends AppCompatActivity {

    EditText newTaskEditText;
    int taskID;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_form);

        newTaskEditText = findViewById(R.id.newTaskEditText);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        Intent intent = getIntent();
        taskID = intent.getIntExtra("inboxID", -1);

        if(taskID != -1){
            InboxTask inboxTask = dataBaseHelper.getInboxTaskByID(taskID);
            newTaskEditText.setText(inboxTask.getTaskDescription());
        }
    }

    public void saveNewTask (View view){
        String inboxTaskDescription = newTaskEditText.getText().toString();
        InboxTask newInboxTask;
        if (taskID != -1){
            newInboxTask = new InboxTask(taskID, inboxTaskDescription);
            dataBaseHelper.updateInboxTaskByID(newInboxTask);
        } else{
            newInboxTask = new InboxTask(-1, inboxTaskDescription);
            dataBaseHelper.addInboxTask(newInboxTask);
        }
        finish();
    }
}
