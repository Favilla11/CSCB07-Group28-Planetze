package com.example.cscb07_project;

import android.graphics.Color;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;


import java.util.ArrayList;

public class PieGraphController {
    ArrayList<Double> line_chart_data;
    public static PieChart mPieChart;

    public PieGraphController(PieChart mPieChart){ //add all data from firebase
        PieGraphController.mPieChart =mPieChart;
    }

    public static void SetChart(){
        PieModel series=new PieModel();

        // TODO: implement
        mPieChart.addPieSlice(new PieModel("Transportation", 15, Color.parseColor("#A9BCD0")));
        mPieChart.addPieSlice(new PieModel("Energy", 25, Color.parseColor("#1b1b1e")));
        mPieChart.addPieSlice(new PieModel("Food Consumption", 35, Color.parseColor("#009999")));
        mPieChart.addPieSlice(new PieModel("Shopping Consumption", 9, Color.parseColor("#373F51")));
        mPieChart.startAnimation();
    }
}
