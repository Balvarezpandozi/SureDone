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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ticklerFile extends AppCompatActivity {

    ImageView generalMenuImageView;
    ListView ticklerFileListView;

    DataBaseHelper dataBaseHelper;
    PopupMenu generalMenu;
    ArrayAdapter ticklerFileAdapter;
    static List<TicklerFileTask> ticklerFileTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickler_file);

        generalMenuImageView = findViewById(R.id.generalMenuImageView);
        ticklerFileListView = findViewById(R.id.ticklerFileListView);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        takeActiveTasksToInbox(dataBaseHelper);

        showTicklerFileTasksOnListView(dataBaseHelper);

        //ITEMS MENU SETUP
        registerForContextMenu(ticklerFileListView);
    }

    //Show tasks on ListView
    private void showTicklerFileTasksOnListView(DataBaseHelper dataBaseHelper1){
        Log.i("is it?", dataBaseHelper1.getAllTicklerFileTasks().toString());
        ticklerFileAdapter = new TicklerFileCustomArrayAdapter(ticklerFile.this, (ArrayList<TicklerFileTask>) dataBaseHelper1.getAllTicklerFileTasks());
        ticklerFileListView.setAdapter(ticklerFileAdapter);
    }

    //Takes tasks back to inbox
    static void takeTaskToInbox(TicklerFileTask ticklerFileTask, DataBaseHelper dataBaseHelper){
        InboxTask inboxTask = new InboxTask(-1, ticklerFileTask.getTitle());
        dataBaseHelper.addInboxTask(inboxTask);
        dataBaseHelper.deleteTicklerFileTaskByID(ticklerFileTask);
    }

    public void takeTaskToInboxButtonAction(TicklerFileTask ticklerFileTask){
        InboxTask inboxTask = new InboxTask(-1, ticklerFileTask.getTitle());
        dataBaseHelper.addInboxTask(inboxTask);
        dataBaseHelper.deleteTicklerFileTaskByID(ticklerFileTask);
        showTicklerFileTasksOnListView(dataBaseHelper);
    }

    //Takes ACTIVE tasks back to the inbox -- Figure out how to do it on background!
    static void takeActiveTasksToInbox(DataBaseHelper dataBaseHelperToTakeBackToInbox){
        ticklerFileTasks = dataBaseHelperToTakeBackToInbox.getAllTicklerFileTasks();
        for (int i = 0; i<ticklerFileTasks.size(); i++){
            String date = ticklerFileTasks.get(i).getActiveDate();
            Calendar now = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/YYYY");
            String nowDate = formatter.format(now.getTime());
            Log.i("date", date + " current: " + nowDate);
            String[] taskDate = date.split("/");
            String[] currentDate = nowDate.split("/");

            if (Integer.parseInt(taskDate[2]) <= Integer.parseInt(currentDate[2])){
                if (Integer.parseInt(taskDate[2]) < Integer.parseInt(currentDate[2])){
                    takeTaskToInbox(ticklerFileTasks.get(i), dataBaseHelperToTakeBackToInbox);
                } else if (Integer.parseInt(taskDate[2]) == Integer.parseInt(currentDate[2])){
                    if (Integer.parseInt(taskDate[0]) <= Integer.parseInt(currentDate[0])){
                        if (Integer.parseInt(taskDate[0]) < Integer.parseInt(currentDate[0])){
                            takeTaskToInbox(ticklerFileTasks.get(i), dataBaseHelperToTakeBackToInbox);
                        }else if (Integer.parseInt(taskDate[0]) == Integer.parseInt(currentDate[0])){
                            if (Integer.parseInt(taskDate[1]) <= Integer.parseInt(currentDate[1])){
                                takeTaskToInbox(ticklerFileTasks.get(i), dataBaseHelperToTakeBackToInbox);
                            }
                        }
                    }
                }
            }
        }
    }

    //GENERAL MENU
    public void openGeneralMenu(View view){
        generalMenu = new PopupMenu(getApplicationContext(), generalMenuImageView);
        generalMenu.inflate(R.menu.generalmenu);
        Menu menu = generalMenu.getMenu();
        menu.findItem(R.id.ticklerfile).setEnabled(false);
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
                        return true;
                    case R.id.settings:
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
        showTicklerFileTasksOnListView(dataBaseHelper);
    }

    // ITEMS MENU SETUP
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ticklerfiletaskmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        TicklerFileTask ticklerFileTask = (TicklerFileTask) ticklerFileListView.getItemAtPosition(position);

        switch (item.getItemId()){
            case R.id.edit:
                Intent ticklerFileFormIntent = new Intent(getApplicationContext(), ticklerFileForm.class);
                ticklerFileFormIntent.putExtra("ID", ticklerFileTask.getId());
                ticklerFileFormIntent.putExtra("fromRequest", "fromTicklerFile");
                startActivity(ticklerFileFormIntent);
                return true;
            case R.id.delete:
                dataBaseHelper.deleteTicklerFileTaskByID(ticklerFileTask);
                showTicklerFileTasksOnListView(dataBaseHelper);
                return true;
            case R.id.takeTaskToInbox:
                takeTaskToInboxButtonAction(ticklerFileTask);
            default:
                return super.onContextItemSelected(item);
        }
    }
}
