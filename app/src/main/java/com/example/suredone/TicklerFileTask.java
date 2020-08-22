package com.example.suredone;

public class TicklerFileTask {

    private int id;
    private String title;
    private String activeDate;

    //Constructor
    public TicklerFileTask(int id, String title, String activeDate) {
        this.id = id;
        this.title = title;
        this.activeDate = activeDate;
    }

    //toString is necessary for printing the contents of a class object
    @Override
    public String toString() {
        return "TicklerFileTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", activeDate='" + activeDate + '\'' +
                '}';
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

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }
}
