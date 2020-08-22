package com.example.suredone;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;

public class HotlistTask {

    private int id;
    private String title;
    private String taskDescription;
    private Boolean doneTask;

    //Constructor
    public HotlistTask(int id, String taskDescription, String title, Boolean doneTask) {
        this.id = id;
        this.taskDescription = taskDescription;
        this.title = title;
        this.doneTask = doneTask;
    }

    //toString is necessary for printing the contents of a class object
    @Override
    public String toString() {
        return title;
    }


    //Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Boolean getDoneTask() {
        return doneTask;
    }

    public void setDoneTask(Boolean doneTask) {
        this.doneTask = doneTask;
    }
}
