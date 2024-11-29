package com.example.cscb07_project;

public class Activity {
    private String date;
    private String description;
    private String category;

    public Activity(String date, String description) {
        this.date = date;
        this.description = description;
    }
    public Activity(String date, String description, String category) {
        this.date = date;
        this.description = description;
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
    public String getCategory(){
        return category;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}