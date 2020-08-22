package com.example.suredone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.List;

public class incubator extends AppCompatActivity {

    ListView incubatorListView;
    ImageView generalMenuImageView;

    PopupMenu generalMenu;
    DataBaseHelper dataBaseHelper;

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incubator);

        incubatorListView = findViewById(R.id.incubatorListView);
        generalMenuImageView = findViewById(R.id.generalMenuImageView);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        showIncubatorTasksOnListView(dataBaseHelper);

        //ITEMS MENU SETUP
        registerForContextMenu(incubatorListView);
    }

    //GENERAL MENU
    public void openGeneralMenu(View view){
        generalMenu = new PopupMenu(getApplicationContext(), generalMenuImageView);
        generalMenu.inflate(R.menu.generalmenu);
        Menu menu = generalMenu.getMenu();
        menu.findItem(R.id.incubator).setEnabled(false);
        generalMenu.show();
        generalMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inbox:
                        Intent inboxIntent = new Intent(getApplicationContext(), MainActivity.class);
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
        inflater.inflate(R.menu.incubatortaskmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        IncubatorTask incubatorTask = (IncubatorTask) incubatorListView.getItemAtPosition(position);

        switch (item.getItemId()){
            case R.id.edit:
                Intent incubatorFormIntent = new Intent(getApplicationContext(), incubatorTaskForm.class);
                incubatorFormIntent.putExtra("ID", incubatorTask.getId());
                incubatorFormIntent.putExtra("fromRequest", "fromIncubator");
                startActivity(incubatorFormIntent);
                return true;
            case R.id.delete:
                dataBaseHelper.deleteIncubatorTaskByID(incubatorTask);
                showIncubatorTasksOnListView(dataBaseHelper);
                return true;
            case R.id.takeTaskToInbox:
                takeTaskToInbox(incubatorTask);
            default:
                return super.onContextItemSelected(item);
        }
    }

    //Take task back to Inbox
    public void takeTaskToInbox(IncubatorTask incubatorTask){
        InboxTask inboxTask = new InboxTask(-1, incubatorTask.getDescription());
        dataBaseHelper.addInboxTask(inboxTask);
        dataBaseHelper.deleteIncubatorTaskByID(incubatorTask);
        showIncubatorTasksOnListView(dataBaseHelper);
    }

    //Take ALL tasks back to Inbox
    public void takeAllTasksToInbox(View view){
        List<IncubatorTask> tasks = dataBaseHelper.getAllIncubatorTasks();
        for (int i = 0; i<tasks.size(); i++){
            takeTaskToInbox(tasks.get(i));
        }
    }

    //On activity resume
    @Override
    protected void onPostResume() {
        showIncubatorTasksOnListView(dataBaseHelper);
        super.onPostResume();
    }

    //show items on list
    public void showIncubatorTasksOnListView(DataBaseHelper dataBaseHelper1){
        adapter = new ArrayAdapter<IncubatorTask>(incubator.this, android.R.layout.simple_list_item_1, dataBaseHelper1.getAllIncubatorTasks());
        incubatorListView.setAdapter(adapter);
    }
}
