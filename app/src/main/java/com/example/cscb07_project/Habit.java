package com.example.cscb07_project;

public class Habit {
    private String category;
    private double progress;
    private String act;
    private double impact;


    public Habit(String category, double progress,String act, double impact){
        this.category = category;
        this.progress = progress;
        this.act = act;
        this.impact = impact;
    }

    public String getCategory() {
        return category;
    }
    public String getAct() {
        return act;
    }

    public double getProgress() {
        return progress;
    }


    public double getImpact() {
        return impact;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setAct(String act){
        this.act = act;
    }

    public void setProgress(double progress){
        this.progress = progress;
    }


    public void setImpact(double impact){
        this.impact = impact;
    }

    public void updateProgress(double update){
        this.progress = this.progress + update;
    }

}