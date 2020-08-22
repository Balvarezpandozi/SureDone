package com.example.suredone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    static ArrayList<String> inbox = new ArrayList<String>();
    static ArrayAdapter inboxAdapter;

    ListView listView;
    ImageView generalMenuImageView;
    Button processTasksButton;

    PopupMenu generalMenu;

    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.inboxTasksListView);
        generalMenuImageView = findViewById(R.id.generalMenuImageView);
        processTasksButton = findViewById(R.id.processTasksButton);
        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        showInboxTasksOnListView(dataBaseHelper);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InboxTask inboxTask = (InboxTask) parent.getItemAtPosition(position);
                editTask(inboxTask);
            }
        });

        //ITEMS MENU SETUP
        registerForContextMenu(listView);
    }

    @Override
    protected void onPostResume() {
        showInboxTasksOnListView(dataBaseHelper);
        super.onPostResume();
    }

    //SET LISTVIEW
    private void showInboxTasksOnListView(DataBaseHelper dataBaseHelper1){
        inboxAdapter = new ArrayAdapter<InboxTask>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper1.getAllInboxTasks());
        listView.setAdapter(inboxAdapter);
    }

    //GENERAL MENU
    public void openGeneralMenu(View view){
        generalMenu = new PopupMenu(getApplicationContext(), generalMenuImageView);
        generalMenu.inflate(R.menu.generalmenu);
        Menu menu = generalMenu.getMenu();
        menu.findItem(R.id.inbox).setEnabled(false);
        generalMenu.show();
        generalMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
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
                    case R.id.settings:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    // ITEMS MENU SETUP
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inboxtaskmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        InboxTask inboxTask = (InboxTask) listView.getItemAtPosition(position);

        switch (item.getItemId()){
            case R.id.edit:
                editTask(inboxTask);
                return true;
            case R.id.processTask:
                processTask(inboxTask);
                return true;
            case R.id.delete:
                deleteTask(inboxTask);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // ITEMS MENU OPTIONS
        //Process Task
    public void processTask(InboxTask inboxTask){
        Intent processTaskIntent = new Intent(getApplicationContext(), processTasks.class);
        processTaskIntent.putExtra("inboxID", inboxTask.getId());
        startActivity(processTaskIntent);
    }
        //Delete
    public void deleteTask(InboxTask inboxTask){
        dataBaseHelper.deleteInboxTaskByID(inboxTask);
        showInboxTasksOnListView(dataBaseHelper);
    }
        //Edit
    public void editTask(InboxTask inboxTask){
        Intent newTaskIntent = new Intent(getApplicationContext(), newTaskForm.class);
        newTaskIntent.putExtra("inboxID", inboxTask.getId());
        startActivity(newTaskIntent);
    }

    //CREATE NEW TASK
    public void createNewTask(View view){
        Intent newTaskIntent = new Intent(getApplicationContext(), newTaskForm.class);
        newTaskIntent.putExtra("inboxID", -1);
        startActivity(newTaskIntent);
    }

    //PROCESS ALL TASKS
    public void  processTasks(View view){
        InboxTask inboxTask;

        for(int i=0; i<inboxAdapter.getCount(); i++){
            inboxTask = (InboxTask) listView.getItemAtPosition(i);
            Intent processTaskIntent = new Intent(getApplicationContext(), processTasks.class);
            processTaskIntent.putExtra("inboxID", inboxTask.getId());
            startActivity(processTaskIntent);
        }
    }
}
