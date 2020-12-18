package com.example.suredone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.suredone.calendar.CalendarTask;
import com.example.suredone.hotlist.HotlistTask;
import com.example.suredone.inbox.InboxTask;
import com.example.suredone.incubator.IncubatorTask;
import com.example.suredone.ticklerFile.TicklerFileTask;

import java.util.ArrayList;
import java.util.List;

//HELPFUL EXAMPLE: https://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/

public class DataBaseHelper extends SQLiteOpenHelper {

    //Database Version
    public static final int DATABASE_VERSION = 4;

    //Database name
    public static final String DATABASE_NAME = "SureDoneDatabase";

    //Table names
    public static final String INBOX_TABLE = "INBOX_TABLE";
    public static final String HOTLIST_TABLE = "HOTLIST_TABLE";
    public static final String TICKLER_FILE_TABLE = "TICKLER_FILE_TABLE";
    public static final String CALENDAR_TABLE = "CALENDAR_TABLE";
    public static final String INCUBATOR_TABLE = "INCUBATOR_TABLE";

    //Common column names
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TASK_DESCRIPTION = "TASK_DESCRIPTION";
    public static final String COLUMN_TASK_TITLE = "TASK_TITLE";

    //Hotlist column names
    public  static final String COLUMN_DONE_TASK = "DONE_TASK";

    //Tickler File column names
    public  static final String COLUMN_DATE_ACTIVE = "DATE_ACTIVE";

    //Colendar column names
    public static final String COLUMN_START_TIME = "START_TIME";
    public static final String COLUMN_END_TIME = "END_TIME";
    public static final String COLUMN_DATE = "DATE";

    //TABLE CREATE STATEMENTS

    //Inbox create statement
    public static final String CREATE_TABLE_INBOX = "CREATE TABLE " + INBOX_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TASK_DESCRIPTION + " TEXT)";

    //Hotlist create statement
    public static final String CREATE_TABLE_HOTLIST = "CREATE TABLE " + HOTLIST_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TASK_DESCRIPTION + " TEXT, " + COLUMN_TASK_TITLE +  " TEXT, " + COLUMN_DONE_TASK + " BOOL)";

    //Tickler File create statement
    public static final String CREATE_TABLE_TICKLERFILE = "CREATE TABLE " + TICKLER_FILE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TASK_TITLE + " TEXT, " + COLUMN_DATE_ACTIVE +  " TEXT)";

    //Calendar create statement
    public static final String CREATE_TABLE_CALENDAR = "CREATE TABLE " + CALENDAR_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TASK_TITLE + " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_START_TIME + " TEXT, " + COLUMN_END_TIME + " TEXT)";

    //Incubator create statement
    public static final String CREATE_TABLE_INCUBATOR = "CREATE TABLE " + INCUBATOR_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TASK_DESCRIPTION + " TEXT)";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating required tables
        db.execSQL(CREATE_TABLE_INBOX);
        db.execSQL(CREATE_TABLE_HOTLIST);
        db.execSQL(CREATE_TABLE_TICKLERFILE);
        db.execSQL(CREATE_TABLE_CALENDAR);
        db.execSQL(CREATE_TABLE_INCUBATOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + INBOX_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HOTLIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TICKLER_FILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALENDAR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCUBATOR_TABLE);

        //Create new tables
        onCreate(db);
    }

    //INBOX METHODS
        //Add new task to the database
    public boolean addInboxTask(InboxTask inboxTask){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TASK_DESCRIPTION, inboxTask.getTaskDescription());

        long insert = db.insert(INBOX_TABLE, null, cv);

        db.close();

        if (insert == -1){
            return false;
        }
        return true;
    }
        //Get all tasks from database
    public List<InboxTask> getAllInboxTasks(){
        List<InboxTask> returnList = new ArrayList<>();
            //get data from database
        String queryString = "SELECT * FROM " + INBOX_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
                //loop through the cursor (result set) and create new InboxTask object. put them into the return list.
            do {
                int inboxTaskID = cursor.getInt(0);
                String inboxTaskDescription = cursor.getString(1);

                InboxTask newInboxTask = new InboxTask(inboxTaskID, inboxTaskDescription);
                returnList.add(newInboxTask);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }
        //Get inbox task by id
    public InboxTask getInboxTaskByID(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + INBOX_TABLE + " WHERE ID = " + ID;
        Cursor cursor = db.rawQuery(queryString, null);
        InboxTask inboxTask = null;

        if (cursor.moveToFirst()){
            int inboxTaskID = cursor.getInt(0);
            String inboxTaskDescription = cursor.getString(1);

            inboxTask = new InboxTask(inboxTaskID, inboxTaskDescription);
        }

        db.close();
        cursor.close();
        return inboxTask;
    }
        //Update inbox task by id
    public boolean updateInboxTaskByID(InboxTask inboxTask){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + INBOX_TABLE + " SET " + COLUMN_TASK_DESCRIPTION + " = '" + inboxTask.getTaskDescription() + "' WHERE " + COLUMN_ID + " = " + inboxTask.getId();
        db.execSQL(queryString);
        db.close();
        return true;
    }
        //Delete inbox task by id
    public boolean deleteInboxTaskByID(InboxTask inboxTask){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + INBOX_TABLE + " WHERE " + COLUMN_ID + " = " + inboxTask.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            db.close();
            cursor.close();
            return true;
        }
        db.close();
        cursor.close();
        return false;
    }


    //HOTLIST METHODS
        //Add new task to the database
    public boolean addHotlistTask(HotlistTask hotlistTask){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TASK_TITLE, hotlistTask.getTitle());
        cv.put(COLUMN_TASK_DESCRIPTION, hotlistTask.getTaskDescription());
        cv.put(COLUMN_DONE_TASK, hotlistTask.getDoneTask());

        long insert = db.insert(HOTLIST_TABLE, null, cv);

        db.close();

        if (insert == -1){
            return false;
        }
        return true;
    }
        //Update hotlist task by id
    public boolean updateHotlistTaskByID(HotlistTask hotlistTask){
        Log.i("title", hotlistTask.getTitle());
        Log.i("description", hotlistTask.getTaskDescription());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String whereString = COLUMN_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(hotlistTask.getId())};

        cv.put(COLUMN_TASK_DESCRIPTION, hotlistTask.getTaskDescription());
        cv.put(COLUMN_TASK_TITLE, hotlistTask.getTitle());
        cv.put(COLUMN_DONE_TASK, hotlistTask.getDoneTask());

        long insert = db.update(HOTLIST_TABLE, cv, whereString, whereArgs);
        db.close();

        if(insert == -1){
            return  false;
        }else{
            return true;
        }
    }
        //Get hotlist task by id
    public HotlistTask getHotlistTaskByID(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + HOTLIST_TABLE + " WHERE ID = " + ID;
        Cursor cursor = db.rawQuery(queryString, null);
        HotlistTask hotlistTask = null;

        if (cursor.moveToFirst()){
            int hotlistTaskID = cursor.getInt(0);
            String hotlistTaskTitle = cursor.getString(1);
            String hotlistTaskDescription = cursor.getString(2);
            boolean hotlistDoneTask = cursor.getInt(3) == 1 ? true:false;

            hotlistTask = new HotlistTask(hotlistTaskID, hotlistTaskTitle, hotlistTaskDescription, hotlistDoneTask);
        }

        db.close();
        cursor.close();
        return hotlistTask;
    }
        //Get all Hotlist tasks from database
    public List<HotlistTask> getAllHotlistTasks(){
        ArrayList<HotlistTask> returnList = new ArrayList<HotlistTask>();
                //get data from database
        String queryString = "SELECT * FROM " + HOTLIST_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
                //loop through the cursor (result set) and create new HotlistTask object. put them into the return list.
            do {
                int hotlistTaskID = cursor.getInt(0);
                String hotlistTaskDescription = cursor.getString(1);
                String hotlistTitle = cursor.getString(2);
                boolean hotlistDone = cursor.getInt(3) == 1 ? true:false;

                HotlistTask newHotlistTask = new HotlistTask(hotlistTaskID, hotlistTaskDescription, hotlistTitle, hotlistDone);
                returnList.add(newHotlistTask);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }
        //Delete hotlist task by id
    public boolean deleteHotlistTaskByID(HotlistTask hotlistTask){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + HOTLIST_TABLE + " WHERE " + COLUMN_ID + " = " + hotlistTask.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        db.close();

        if (cursor.moveToFirst()){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
        //Delete hotlist task by done task = true
    public boolean deleteHotlistTaskByDone(){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + HOTLIST_TABLE + " WHERE " + COLUMN_DONE_TASK + " = 1";
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            db.close();
            cursor.close();
            return true;
        }
        db.close();
        cursor.close();
        return false;
    }

    //TICKLER FILE METHODS
        //Add new tickler file task to the database
    public boolean addTicklerFileTask(TicklerFileTask ticklerFileTask){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TASK_TITLE, ticklerFileTask.getTitle());
        cv.put(COLUMN_DATE_ACTIVE, ticklerFileTask.getActiveDate());

        long insert = db.insert(TICKLER_FILE_TABLE, null, cv);

        db.close();
        if (insert == -1){
            return false;
        }
        return true;
    }
        //Update tickler file task by id
    public boolean updateTicklerFileTaskByID(TicklerFileTask ticklerFileTask){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            String whereString = COLUMN_ID + " = ?";
            String[] whereArgs = new String[] {String.valueOf(ticklerFileTask.getId())};

            cv.put(COLUMN_TASK_TITLE, ticklerFileTask.getTitle());
            cv.put(COLUMN_DATE_ACTIVE, ticklerFileTask.getActiveDate());

            long insert = db.update(TICKLER_FILE_TABLE, cv, whereString, whereArgs);
            db.close();

            if(insert == -1){
                return  false;
            }else{
                return true;
            }
        }
        //Get tickler file task by id
    public TicklerFileTask getTicklerFileTaskByID(int ID){
            SQLiteDatabase db = this.getReadableDatabase();
            String queryString = "SELECT * FROM " + TICKLER_FILE_TABLE + " WHERE ID = " + ID;
            Cursor cursor = db.rawQuery(queryString, null);
            TicklerFileTask ticklerFileTask = null;

            if (cursor.moveToFirst()){
                int ticklerFileTaskID = cursor.getInt(0);
                String ticklerFileTaskTitle = cursor.getString(1);
                String ticklerFileTaskDate = cursor.getString(2);

                ticklerFileTask = new TicklerFileTask(ticklerFileTaskID, ticklerFileTaskTitle, ticklerFileTaskDate);
            }

            db.close();
            cursor.close();
            return ticklerFileTask;
        }
        //Get all tickler file tasks from database
    public List<TicklerFileTask> getAllTicklerFileTasks(){
        ArrayList<TicklerFileTask> returnList = new ArrayList<TicklerFileTask>();
        //get data from database
        String queryString = "SELECT * FROM " + TICKLER_FILE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            //loop through the cursor (result set) and create new HotlistTask object. put them into the return list.
            do {
                int ticklerFileTaskID = cursor.getInt(0);
                String ticklerFileTaskTitle = cursor.getString(1);
                String ticklerFileTaskDate = cursor.getString(2);

                TicklerFileTask newTicklerFileTask = new TicklerFileTask(ticklerFileTaskID, ticklerFileTaskTitle, ticklerFileTaskDate);
                returnList.add(newTicklerFileTask);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }
        //Delete tickler file task by id
    public boolean deleteTicklerFileTaskByID(TicklerFileTask ticklerFileTask){
            SQLiteDatabase db = this.getWritableDatabase();
            String queryString = "DELETE FROM " + TICKLER_FILE_TABLE + " WHERE " + COLUMN_ID + " = " + ticklerFileTask.getId();
            Cursor cursor = db.rawQuery(queryString, null);

            if (cursor.moveToFirst()){
                db.close();
                cursor.close();
                return true;
            }
            db.close();
            cursor.close();
            return false;
        }

    //CALENDAR METHODS
        //Add new tickler file task to the database
    public boolean addCalendarTask(CalendarTask calendarTask){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TASK_TITLE, calendarTask.getTitle());
        cv.put(COLUMN_DATE, calendarTask.getDate());
        cv.put(COLUMN_START_TIME, calendarTask.getStartTime());
        cv.put(COLUMN_END_TIME, calendarTask.getEndTime());

        long insert = db.insert(CALENDAR_TABLE, null, cv);

        db.close();

        if (insert == -1){
            return false;
        }
        return true;
    }
        //Update tickler file task by id
    public boolean updateCalendarTaskByID(CalendarTask calendarTask){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String whereString = COLUMN_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(calendarTask.getId())};

        cv.put(COLUMN_TASK_TITLE, calendarTask.getTitle());
        cv.put(COLUMN_DATE, calendarTask.getDate());
        cv.put(COLUMN_START_TIME, calendarTask.getStartTime());
        cv.put(COLUMN_END_TIME, calendarTask.getEndTime());

        long insert = db.update(CALENDAR_TABLE, cv, whereString, whereArgs);
        db.close();

        if(insert == -1){
            return  false;
        }else{
            return true;
        }
    }
        //Get tickler file task by id
    public CalendarTask getCalendarTaskByID(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + CALENDAR_TABLE + " WHERE ID = " + ID;
        Cursor cursor = db.rawQuery(queryString, null);
        CalendarTask calendarTask = null;

        if (cursor.moveToFirst()){
            int calendarTaskID = cursor.getInt(0);
            String calendarTaskTitle = cursor.getString(1);
            String calendarTaskDate = cursor.getString(2);
            String calendarTaskStartTime = cursor.getString(3);
            String calendarTaskEndTime = cursor.getString(4);

            calendarTask = new CalendarTask(calendarTaskID, calendarTaskTitle, calendarTaskDate, calendarTaskStartTime, calendarTaskEndTime);
        }

        db.close();
        cursor.close();
        return calendarTask;
    }
        //Get all tickler file tasks from database
    public List<CalendarTask> getAllCalendarTasks(){
        ArrayList<CalendarTask> returnList = new ArrayList<CalendarTask>();
        //get data from database
        String queryString = "SELECT * FROM " + CALENDAR_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            //loop through the cursor (result set) and create new HotlistTask object. put them into the return list.
            do {
                int calendarTaskID = cursor.getInt(0);
                String calendarTaskTitle = cursor.getString(1);
                String calendarTaskDate = cursor.getString(2);
                String calendarTaskStartTime = cursor.getString(3);
                String calendarTaskEndTime = cursor.getString(4);

                CalendarTask newCalendarTask = new CalendarTask(calendarTaskID, calendarTaskTitle, calendarTaskDate, calendarTaskStartTime, calendarTaskEndTime);
                returnList.add(newCalendarTask);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }
        //Delete tickler file task by id
    public boolean deleteCalendarTaskByID(CalendarTask calendarTask){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CALENDAR_TABLE + " WHERE " + COLUMN_ID + " = " + calendarTask.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            db.close();
            cursor.close();
            return true;
        }
        db.close();
        cursor.close();
        return false;
    }

    //INCUBATOR METHODS
        //Add incubator task
    public boolean addIncubatorTask(IncubatorTask incubatorTask){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TASK_DESCRIPTION, incubatorTask.getDescription());

        long insert = db.insert(INCUBATOR_TABLE, null, cv);

        db.close();

        if (insert == -1){
            return false;
        }
        return true;
    }
        //Get all incubator tasks
    public List<IncubatorTask> getAllIncubatorTasks(){
        List<IncubatorTask> returnList = new ArrayList<>();
        //get data from database
        String queryString = "SELECT * FROM " + INCUBATOR_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            //loop through the cursor (result set) and create new InboxTask object. put them into the return list.
            do {
                int incubatorTaskID = cursor.getInt(0);
                String incubatorTaskDescription = cursor.getString(1);

                IncubatorTask newIncubatorTask = new IncubatorTask(incubatorTaskID, incubatorTaskDescription);
                returnList.add(newIncubatorTask);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }
        //Get incubator task by id
    public IncubatorTask getIncubatorTaskByID(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + INCUBATOR_TABLE + " WHERE ID = " + ID;
        Cursor cursor = db.rawQuery(queryString, null);
        IncubatorTask incubatorTask = null;

        if (cursor.moveToFirst()){
            int incubatorTaskID = cursor.getInt(0);
            String incubatorTaskDescription = cursor.getString(1);

            incubatorTask = new IncubatorTask(incubatorTaskID, incubatorTaskDescription);
        }

        db.close();
        cursor.close();
        return incubatorTask;
    }
        //Update incubator task by id
    public boolean updateIncubatorTaskByID(IncubatorTask incubatorTask){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + INCUBATOR_TABLE + " SET " + COLUMN_TASK_DESCRIPTION + " = '" + incubatorTask.getDescription() + "' WHERE " + COLUMN_ID + " = " + incubatorTask.getId();
        db.execSQL(queryString);
        db.close();
        return true;
    }
        //Delete incubator task by id
    public boolean deleteIncubatorTaskByID(IncubatorTask incubatorTask){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + INCUBATOR_TABLE + " WHERE " + COLUMN_ID + " = " + incubatorTask.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            db.close();
            cursor.close();
            return true;
        }
        db.close();
        cursor.close();
         return false;
    }
}
