package com.example.cscb07_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        String countryName = (String) getIntent().getSerializableExtra("countryName");
        double userFootprint = getIntent().getDoubleExtra("userFootprint", 0.0);
        double countryAverage = Double.parseDouble(getIntent().getStringExtra("countryAverage"));
        double globalTarget = 1000.0;

        double comparisonToCountry = ((userFootprint - countryAverage) / countryAverage) * 100;
        double comparisonToGlobal = ((userFootprint - globalTarget) / globalTarget) * 100;

        TextView countryComparisonText = findViewById(R.id.countryComparisonText);
        TextView globalComparisonText = findViewById(R.id.globalComparisonText);
        Button finishButton = findViewById(R.id.btn_goToHome);
        String countryComparisonResult = String.format("Your carbon footprint is %.2f%% %s the national average for %s.",
                Math.abs(comparisonToCountry),
                comparisonToCountry < 0 ? "below" : "above",
                countryName);
        String globalComparisonResult = String.format("Your carbon footprint is %.2f%% %s the global target.",
                Math.abs(comparisonToGlobal),
                comparisonToGlobal < 0 ? "below" : "above");

        countryComparisonText.setText(countryComparisonResult);
        globalComparisonText.setText(globalComparisonResult);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompareActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
