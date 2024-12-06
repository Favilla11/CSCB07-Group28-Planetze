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

    public PieGraphController(PieChart mPieChart){
        PieGraphController.mPieChart =mPieChart;
    }

    public static void SetChart(float Transportation, float Housing, float Food, float Shopping){
        PieModel series=new PieModel();

        // TODO: implement
        mPieChart.addPieSlice(new PieModel("Transportation", Transportation, Color.parseColor("#A9BCD0")));
        mPieChart.addPieSlice(new PieModel("Housing", Housing, Color.parseColor("#1b1b1e")));
        mPieChart.addPieSlice(new PieModel("Food Consumption", Food, Color.parseColor("#009999")));
        mPieChart.addPieSlice(new PieModel("Shopping Consumption", Shopping, Color.parseColor("#373F51")));
        mPieChart.startAnimation();
    }
}