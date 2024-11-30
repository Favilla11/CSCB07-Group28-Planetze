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
        mPieChart.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Sleep", 25, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Work", 35, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Eating", 9, Color.parseColor("#FED70E")));
        mPieChart.startAnimation();
    }
}
