package com.example.suredone;

public class InboxTask {
    private int id;
    private String taskDescription;

    //Constructor
    public InboxTask(int id, String taskDescription) {
        this.id = id;
        this.taskDescription = taskDescription;
    }

    //toString is necessary for printing the contents of a class object

    @Override
    public String toString() {
        return taskDescription;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskContent) {
        this.taskDescription = taskContent;
    }
}
