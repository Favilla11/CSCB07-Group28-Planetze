package com.example.cscb07_project;

import android.util.Log;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

// sets data retrieved from Retriever to graph
public class LineChartController {
    // TODO: get actual data from firebase
    public ValueLineChart mCubicValueLineChart;
    public ValueLineSeries series;
    public LineChartController(ValueLineChart mCubicValueLineChart){ //add all data from firebase

        // TODO: needs implementation

        this.mCubicValueLineChart=mCubicValueLineChart;
    }

    public void setChart(ArrayList<Double> emissionArr, ArrayList<String> dateArr){
        series = new ValueLineSeries();
        series.setColor(0xFF009999);
        Double doubleValue = 123.45;
        float floatValue = doubleValue.floatValue();
//        Calendar calendar = Calendar.getInstance();
        int len=emissionArr.size();
        for(int i=0;i<len;i++){
            double dblEmission=emissionArr.get(i);
            series.addPoint(new ValueLinePoint(dateArr.get(i), (float) dblEmission));
        }

//        int length=userInformation.size();
//        String date="";
//        Set<String> allSetDate = new HashSet<>(userInformation.keySet());
//        ArrayList<String> allDate = new ArrayList<>(allSetDate);

//        for(int i=0; i<length; i++){
//            date=allDate.get(i);
//            series.addPoint(new ValueLinePoint(date, (float)userInformation.get(date).getDailyEmission()));
//        }
        /*series.addPoint(new ValueLinePoint("Jan", 2.4f));
        series.addPoint(new ValueLinePoint("Feb", 3.4f));
        series.addPoint(new ValueLinePoint("Mar", .4f));
        series.addPoint(new ValueLinePoint("Apr", 1.2f));
        series.addPoint(new ValueLinePoint("Mai", 2.6f));
        series.addPoint(new ValueLinePoint("Jun", 1.0f));
        series.addPoint(new ValueLinePoint("Jul", 3.5f));
        series.addPoint(new ValueLinePoint("Aug", 2.4f));
        series.addPoint(new ValueLinePoint("Sep", 2.4f));
        series.addPoint(new ValueLinePoint("Oct", 3.4f));
        series.addPoint(new ValueLinePoint("Nov", .4f));
        series.addPoint(new ValueLinePoint("Dec", 1.3f));*/

        this.mCubicValueLineChart.addSeries(series);
        this.mCubicValueLineChart.startAnimation();

    }


}