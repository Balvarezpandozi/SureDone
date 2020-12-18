package com.example.suredone.hotlist;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.suredone.DataBaseHelper;
import com.example.suredone.R;
import com.example.suredone.calendar.calendar;
import com.example.suredone.inbox.inbox;
import com.example.suredone.incubator.incubator;
import com.example.suredone.info.Info;
import com.example.suredone.ticklerFile.ticklerFile;

import java.util.ArrayList;

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;

public class hotlist extends AppCompatActivity {

    ListView listView;
    ImageView generalMenuImageView;
    ArrayAdapter hotlistAdapter;

    PopupMenu generalMenu;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotlist);

        listView = findViewById(R.id.hotlistListView);
        generalMenuImageView = findViewById(R.id.generalMenuImageView);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        showHotlistTasksOnListView(dataBaseHelper);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotlistTask hotlistTask = (HotlistTask) parent.getItemAtPosition(position);
                TextView t = (TextView) view;
                if (hotlistTask.getDoneTask().equals(true)){
                    t.setPaintFlags(t.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }else if (hotlistTask.getDoneTask().equals(false)){
                    t.setPaintFlags(t.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                }
                setDone(hotlistTask);
            }
        });

        //ITEMS MENU SETUP
        registerForContextMenu(listView);
    }

    //On Resume update listview
    @Override
    protected void onPostResume() {
        showHotlistTasksOnListView(dataBaseHelper);
        super.onPostResume();
    }

    //SET LISTVIEW
    private void showHotlistTasksOnListView(DataBaseHelper dataBaseHelper1){
        hotlistAdapter = new HotlistCustomArrayAdapter(hotlist.this, (ArrayList<HotlistTask>) dataBaseHelper1.getAllHotlistTasks());
        listView.setAdapter(hotlistAdapter);
    }

    public void setDone(HotlistTask hotlistTask){
        if (hotlistTask.getDoneTask()){
            hotlistTask.setDoneTask(false);
            dataBaseHelper.updateHotlistTaskByID(hotlistTask);
        }else{
            hotlistTask.setDoneTask(true);
            dataBaseHelper.updateHotlistTaskByID(hotlistTask);
        }
        showHotlistTasksOnListView(dataBaseHelper);
    }

    //GENERAL MENU
    public void openGeneralMenu(View view){
        generalMenu = new PopupMenu(getApplicationContext(), generalMenuImageView);
        generalMenu.inflate(R.menu.generalmenu);
        Menu menu = generalMenu.getMenu();
        menu.findItem(R.id.hotlist).setEnabled(false);
        generalMenu.show();
        generalMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inbox:
                        Intent inboxIntent = new Intent(getApplicationContext(), inbox.class);
                        startActivity(inboxIntent);
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
                    case R.id.info:
                        Intent infoIntent = new Intent(getApplicationContext(), Info.class);
                        startActivity(infoIntent);
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
        inflater.inflate(R.menu.hotlisttaskmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        HotlistTask hotlistTask = (HotlistTask) listView.getItemAtPosition(position);

        switch (item.getItemId()){
            case R.id.edit:
                Intent hotlistFormIntent = new Intent(getApplicationContext(), hotlistTaskForm.class);
                hotlistFormIntent.putExtra("ID", hotlistTask.getId());
                hotlistFormIntent.putExtra("fromRequest", "hotlist");
                startActivity(hotlistFormIntent);
                return true;
            case R.id.delete:
                dataBaseHelper.deleteHotlistTaskByID(hotlistTask);
                showHotlistTasksOnListView(dataBaseHelper);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //DELETE DONE TASKS
    public void deleteDoneTasks(View view){
        dataBaseHelper.deleteHotlistTaskByDone();
        showHotlistTasksOnListView(dataBaseHelper);
    }
}
