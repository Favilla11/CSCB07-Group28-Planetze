
package com.example.cscb07_project;

import java.util.List;

public class Information {
    private List<Activity> activityList;
    private double dailyEmission;

    public Information(List<Activity> activityList, double dailyEmission){
        this.activityList = activityList;
        this.dailyEmission = dailyEmission;
    }

    public double getDailyEmission() {
        return dailyEmission;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public void setDailyEmission(double dailyEmission) {
        this.dailyEmission = dailyEmission;
    }
}
