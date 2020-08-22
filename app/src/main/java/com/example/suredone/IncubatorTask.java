package com.example.suredone;

public class IncubatorTask {

    private int id;
    private String description;

    //Constructor
    public IncubatorTask(int id, String title) {
        this.id = id;
        this.description = title;
    }

    //toString method
    @Override
    public String toString() {
        return description;
    }

    //Setters and Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String title) {
        this.description = title;
    }
}
