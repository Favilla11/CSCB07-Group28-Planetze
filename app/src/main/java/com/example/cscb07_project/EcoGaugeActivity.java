package com.example.cscb07_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.charts.PieChart;

public class EcoGaugeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eco_gauge);

        ValueLineChart mCubicValueLineChart = findViewById(R.id.cubiclinechart);
        LineChartController lineChartController=new LineChartController(mCubicValueLineChart);
        lineChartController.SetChart();

        PieChart mPieChart = findViewById(R.id.piechart);
        PieGraphController pieChartController=new PieGraphController(mPieChart);
        PieGraphController.SetChart();

        TextView totalEmission=findViewById(R.id.TotalEmission);
        // TODO: get value from firebase
        totalEmission.setText("the value is");

        // TODO: get value from firebase
        TextView globalComparison=findViewById(R.id.GlobalComparison);
        globalComparison.setText("the comparison is ");


    }

}