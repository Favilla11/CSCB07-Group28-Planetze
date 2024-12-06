package com.example.cscb07_project;

public class Habit {
    private String category;
    private double progress;
    private String action;

    private double impact;

    private double frequency;

    public Habit(){}
    public Habit(String category, double progress, String act, double impact,double frequency){
        this.category = category;
        this.progress = progress;
        this.action = act;
        this.impact = impact;
        this.frequency = frequency;
    }

    public String getCategory() {
        return category;
    }
    public String getAct() {
        return action;
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
        this.action = act;
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