package com.example.cscb07_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DisplayResultActivity extends AppCompatActivity {
    private TextView totalValue, transportationValue, foodValue, housingValue, consumptionValue;
    private double total, transportPercent, foodPercent, housingPercent, consumptionPercent;
    private String totalFootprint, transportFootprint, foodFootprint, housingFootprint, consumptionFootprint;
    private ArrayList<String[]> graph;
    private Button nextButton;
    public String countryName, countryAverage;
    private DatabaseReference db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        totalValue = findViewById(R.id.totalValue);
        transportationValue = findViewById(R.id.transportationValue);
        foodValue = findViewById(R.id.foodValue);
        housingValue = findViewById(R.id.housingValue);
        consumptionValue = findViewById(R.id.consumptionValue);
        nextButton = findViewById(R.id.btn_next);

        countryName = getIntent().getStringExtra("countryName");
        countryAverage = getIntent().getStringExtra("countryAverage");

        HashMap<Integer, String> storedAnswer = (HashMap<Integer, String>) getIntent().getSerializableExtra("userResponse");
        graph = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getAssets().open("formulas.csv")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                graph.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        total = calculateFootprint(storedAnswer, graph).get(0);
        transportPercent = calculateFootprint(storedAnswer, graph).get(1);
        foodPercent = calculateFootprint(storedAnswer, graph).get(2);
        housingPercent = calculateFootprint(storedAnswer, graph).get(3);
        consumptionPercent = calculateFootprint(storedAnswer, graph).get(4);
        if (housingPercent <= 0){
            housingPercent = 0;
        }

        totalFootprint = String.format("%.2f kg CO₂", calculateFootprint(storedAnswer, graph).get(0));
        transportFootprint = String.format("%.2f kg CO₂ (%.2f%%)", calculateFootprint(storedAnswer, graph).get(1),transportPercent / total * 100);
        foodFootprint = String.format("%.2f kg CO₂ (%.2f%%)", calculateFootprint(storedAnswer, graph).get(2),foodPercent/ total * 100);
        housingFootprint = String.format("%.2f kg CO₂ (%.2f%%)", calculateFootprint(storedAnswer, graph).get(3),housingPercent/ total * 100);
        consumptionFootprint = String.format("%.2f kg CO₂ (%.2f%%)", calculateFootprint(storedAnswer, graph).get(4),consumptionPercent/ total * 100);
        displayResult();
        db = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.child(currentUser.getUid()).child("totalFootprint").setValue(total);
                db.child(currentUser.getUid()).child("transportationFootprint").setValue(transportPercent);
                db.child(currentUser.getUid()).child("foodFootprint").setValue(foodPercent);
                db.child(currentUser.getUid()).child("houseFootprint").setValue(housingPercent);
                db.child(currentUser.getUid()).child("consumptionFootprint").setValue(consumptionPercent);


                navigateToCompare();
            }
        });

    }
    private ArrayList<Double> calculateFootprint(HashMap<Integer,String> storedAnswer, ArrayList<String[]> graph){
        double footprint = 0;
        double transportFootprint = 0;
        double foodFootprint = 0;
        double housingFootprint = 0;
        double consumptionFootprint = 0;

        if(storedAnswer.get(1).equals("Yes"))
        {
            double emmision = 0;
            if(storedAnswer.get(2).equals("Gasoline"))
            {
                emmision = 0.24;
            }
            else if(storedAnswer.get(2).equals("Diesel"))
            {
                emmision = 0.27;
            }
            else if(storedAnswer.get(2).equals("Hybrid"))
            {
                emmision = 0.16;
            }

            else if(storedAnswer.get(2).equals("Electric"))
            {
                emmision = 0.05;
            }

            double dist = 0;

            if (storedAnswer.get(3).equals("<= 5,000 km"))
            {
                dist = 5000;
            }

            else if (storedAnswer.get(3).equals("5,000–10,000 km"))
            {
                dist = 10000;
            }
            else if (storedAnswer.get(3).equals("10,000–15,000 km"))
            {
                dist = 15000;
            }
            else if (storedAnswer.get(3).equals("15,000–20,000 km"))
            {
                dist = 20000;
            }

            else if (storedAnswer.get(3).equals("20,000–25,000 km"))
            {
                dist = 25000;
            }
            else if (storedAnswer.get(3).equals(">=25,000 km"))
            {
                dist = 35000;
            }
            transportFootprint = emmision * dist;
        }
        int column = 0;
        int row = 0;
        boolean transport = false;


        if (storedAnswer.get(4).equals("Occasionally (1-2 times/week)"))
        {
            column = 1;
            transport = true;
        }

        else if (storedAnswer.get(4).equals("Frequently (3-4 times/week)")|| storedAnswer.get(4).equals("Always (5+ times/week)"))
        {
            column = 2;
            transport = true;
        }

        if(storedAnswer.get(5).equals("Under 1 hour"))
        {
            row = 1;
        }
        else if(storedAnswer.get(5).equals("1-3 hours"))
        {
            row = 2;
        }
        else if(storedAnswer.get(5).equals("3-5 hours"))
        {
            row = 3;
        }
        else if(storedAnswer.get(5).equals("5-10 hours"))
        {
            row = 4;
        }
        else if(storedAnswer.get(5).equals("More than 10 hours"))
        {
            row = 5;
        }


        if(transport)
        {
            transportFootprint = transportFootprint + Double.parseDouble(graph.get(row)[column]);
        }

        if(storedAnswer.get(6).equals("1-2 flights"))
        {
            transportFootprint = transportFootprint + 225;
        }

        else if(storedAnswer.get(6).equals("3-5 flights"))
        {
            transportFootprint = transportFootprint + 600;
        }

        else if(storedAnswer.get(6).equals("6-10 flights"))
        {
            transportFootprint = transportFootprint + 1200;
        }
        else if(storedAnswer.get(6).equals("More than 10 flights"))
        {
            transportFootprint = transportFootprint + 1800;
        }

        if(storedAnswer.get(7).equals("1-2 flights"))
        {
            transportFootprint = transportFootprint + 825;
        }

        else if(storedAnswer.get(7).equals("3-5 flights"))
        {
            transportFootprint = transportFootprint + 2200;
        }

        else if(storedAnswer.get(7).equals("6-10 flights"))
        {
            transportFootprint = transportFootprint + 4400;
        }
        else if(storedAnswer.get(7).equals("More than 10 flights"))
        {
            transportFootprint = transportFootprint + 6600;
        }
        footprint += transportFootprint;

        if(storedAnswer.get(8).equals("Vegetarian"))
        {
            foodFootprint = foodFootprint + 1000;
        }

        else if(storedAnswer.get(8).equals("Vegan"))
        {
            foodFootprint = foodFootprint + 500;
        }

        else if(storedAnswer.get(8).equals("Pescatarian (fish/seafood)"))
        {
            foodFootprint = foodFootprint + 1500;
        }
        else {
            String[] meat = storedAnswer.get(9).split(":");
            boolean eat = false;
            for(int i = 1; i <= 4; i++)
            {

                if(meat[i].substring(1,2).equals("D"))
                {
                    column = 4;
                    eat = true;
                }

                else if(meat[i].substring(1,2).equals("F"))
                {
                    column = 5;
                    eat = true;
                }
                else if(meat[i].substring(1,2).equals("O"))
                {
                    column = 6;
                    eat = true;
                }
                if(eat)
                {
                    foodFootprint = foodFootprint + Double.parseDouble(graph.get(i)[column]);
                }
            }
        }

        if(storedAnswer.get(10).equals("Rarely"))
        {
            foodFootprint = foodFootprint + 23.4;
        }

        else if(storedAnswer.get(10).equals("Occasionally"))
        {
            foodFootprint = foodFootprint + 70.2;
        }

        else if(storedAnswer.get(10).equals("Frequently"))
        {
            foodFootprint = foodFootprint + 140.4;
        }
        footprint += foodFootprint;
        boolean energy = true;



        if(storedAnswer.get(11).equals("Detached house"))
        {
            row = 7;
        }

        else if(storedAnswer.get(11).equals("Semi-detached house"))
        {
            row = 25;
        }

        else if(storedAnswer.get(11).equals("Townhouse"))
        {
            row = 43;
        }

        else if(storedAnswer.get(11).equals("Condo/Apartment"))
        {
            row = 61;
        }

        if(storedAnswer.get(12).equals("2"))
        {
            row = row + 1;
        }

        else if(storedAnswer.get(12).equals("3-4"))
        {
            row = row + 2;
        }

        else if(storedAnswer.get(12).equals("5 or more"))
        {
            row = row + 3;
        }

        if(storedAnswer.get(13).equals("1000-2000 sq. ft."))
        {
            row = row + 6;
        }

        else if(storedAnswer.get(13).equals("Over 2000 sq. ft."))
        {
            row = row + 12;
        }

        if(storedAnswer.get(14).equals("Natural Gas"))
        {
            column = 2;
        }

        else if(storedAnswer.get(14).equals("Electricity"))
        {
            column = 3;
        }

        else if(storedAnswer.get(14).equals("Oil"))
        {
            column = 4;
        }

        else if(storedAnswer.get(14).equals("Propane"))
        {
            column = 5;
        }

        else if(storedAnswer.get(14).equals("Wood"))
        {
            column = 6;
        }

        if(storedAnswer.get(14).equals("Other"))
        {
            energy = false;
        }


        if(storedAnswer.get(15).equals("$50-$100"))
        {
            column = column + 5;
        }
        else if(storedAnswer.get(15).equals("$100-$150"))
        {
            column = column + 10;
        }
        else if(storedAnswer.get(15).equals("$150-$200"))
        {
            column = column + 15;
        }
        else if(storedAnswer.get(15).equals("Over $200"))
        {
            column = column + 20;
        }

        if(energy)
        {
            housingFootprint = housingFootprint + Double.parseDouble(graph.get(row)[column]);
        }

        if(!storedAnswer.get(14).equals(storedAnswer.get(16)))
        {
            housingFootprint = housingFootprint + 233;
        }

        if(storedAnswer.get(17).equals("Yes, primarily (more than 50% of energy use)"))
        {
            housingFootprint = housingFootprint - 600;
        }

        else if(storedAnswer.get(17).equals("Yes, partially (less than 50% of energy use)"))
        {
            housingFootprint = housingFootprint - 400;
        }
        footprint += housingFootprint;
        double product = 0;
        boolean reduct = true;
        if(storedAnswer.get(18).equals("Monthly"))
        {
            product = 360;
            row = 79;
        }

        else if(storedAnswer.get(18).equals("Quarterly"))
        {
            product = 120;
            reduct = false;
        }

        else if(storedAnswer.get(18).equals("Annually"))
        {
            product = 100;
            row = 80;
        }

        else if(storedAnswer.get(18).equals("Rarely"))
        {
            product = 5;
            row = 81;
        }

        if(storedAnswer.get(19).equals("Yes, regularly"))
        {
            product = product*0.5;
        }

        else if(storedAnswer.get(19).equals("Yes, occasionally"))
        {
            product = product*0.3;
        }

        consumptionFootprint = consumptionFootprint + product;

        if(storedAnswer.get(20).equals("1"))
        {
            consumptionFootprint = consumptionFootprint + 300;
        }

        else if(storedAnswer.get(20).equals("2"))
        {
            consumptionFootprint = consumptionFootprint + 600;
        }

        else if(storedAnswer.get(20).equals("3 or more"))
        {
            consumptionFootprint = consumptionFootprint + 1200;
        }

        if(storedAnswer.get(21).equals("Occasionally"))
        {

            column = 1;
        }

        else if(storedAnswer.get(21).equals("Frequently"))
        {
            column = 2;
        }

        else if(storedAnswer.get(21).equals("Always"))
        {
            column = 3;
        }

        if(reduct)
        {
            consumptionFootprint = consumptionFootprint - Double.parseDouble(graph.get(row)[column]);
        }
        footprint += consumptionFootprint;

        return new ArrayList<>(Arrays.asList(footprint, transportFootprint, foodFootprint, housingFootprint, consumptionFootprint));

    }
    private void displayResult(){
        totalValue.setText(totalFootprint);
        transportationValue.setText(transportFootprint);
        foodValue.setText(foodFootprint);
        housingValue.setText(housingFootprint);
        consumptionValue.setText(consumptionFootprint);
    }
    private void navigateToCompare(){
        String totalStr = String.valueOf(total);
        Intent intent = new Intent(DisplayResultActivity.this, CompareActivity.class);
        intent.putExtra("totalFootprint", totalStr);
        intent.putExtra("countryAverage", countryAverage);
        intent.putExtra("countryName", countryName);
        startActivity(intent);
        finish();
    }

}