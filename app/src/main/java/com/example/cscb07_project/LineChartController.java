package com.example.cscb07_project;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;

// sets data retrieved from Retriever to graph
public class LineChartController {
// TODO: get actual data from firebase
    ArrayList<Double> line_chart_data;
    public ValueLineChart mCubicValueLineChart;
    public LineChartController(ValueLineChart mCubicValueLineChart){ //add all data from firebase

        // TODO: needs implementation

        this.mCubicValueLineChart=mCubicValueLineChart;
    }

    public void SetChart(){
        ValueLineSeries series = new ValueLineSeries();
        // TODO: change colour
        series.setColor(0xFF56B7F1);

        // TODO: implement
        series.addPoint(new ValueLinePoint("Jan", 2.4f)); // for testing
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
        series.addPoint(new ValueLinePoint("Dec", 1.3f));

        this.mCubicValueLineChart.addSeries(series);
        this.mCubicValueLineChart.startAnimation();
    }


}
