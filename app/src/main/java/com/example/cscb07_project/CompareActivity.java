package com.example.cscb07_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class CompareActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compare);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String countryName = getIntent().getStringExtra("countryName");
        String totalFootprintStr = getIntent().getStringExtra("totalFootprint");
        String countryAverageStr = getIntent().getStringExtra("countryAverage");
//        double userFootprint = Double.parseDouble(totalFootprintStr);
//        double countryAverage = Double.parseDouble(countryAverageStr);
//

        double globalTarget = 1000.0;

//        double comparisonToCountry = ((userFootprint - countryAverage) / countryAverage) * 100;
//        double comparisonToGlobal = ((userFootprint - globalTarget) / globalTarget) * 100;
//
        TextView countryComparisonText = findViewById(R.id.countryComparisonText);
        TextView globalComparisonText = findViewById(R.id.globalComparisonText);
        Button finishButton = findViewById(R.id.btn_goToHome);

//        String countryComparisonResult = String.format("Your carbon footprint is %.2f%% %s the national average for %s.",
//                Math.abs(comparisonToCountry),
//                comparisonToCountry < 0 ? "below" : "above",
//                countryName);
//        String globalComparisonResult = String.format("Your carbon footprint is %.2f%% %s the global target.",
//                Math.abs(comparisonToGlobal),
//                comparisonToGlobal < 0 ? "below" : "above");

        countryComparisonText.setText("hi");
        globalComparisonText.setText("hi");

        // Handle button click
        finishButton.setOnClickListener(v -> {
            Log.d("Footprint Value", "Footprint string is: " + countryAverageStr);
//            Log.d("Footprint Value", "Footprint string is: " + countryAverage);
            if(totalFootprintStr==null){
                Toast.makeText(CompareActivity.this, "invlaid format", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(CompareActivity.this, "valid format", Toast.LENGTH_SHORT).show();
            };
//            Intent intent = new Intent(CompareActivity.this, HomeActivity.class);
//            startActivity(intent);
//            finish();
        });
    }
}