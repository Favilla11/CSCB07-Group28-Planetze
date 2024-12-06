package com.example.cscb07_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class EcoGaugeActivity extends AppCompatActivity {

    private double Transportation, Housing, Food, Shopping;
    private double totalEmissions;
    private double yearlyEmission;
    private Map<String, Information> userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("EcoGauge","created");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eco_gauge);
        TextView totalEmission=findViewById(R.id.TotalEmission);
        ValueLineChart mCubicValueLineChart = findViewById(R.id.cubiclinechart);
        ArrayList<String> dateArr = new ArrayList<>();
        ArrayList<Double> emissionArr = new ArrayList<>();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EcoGaugeActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userId = currentUser.getUid();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
                if (user != null) {
                    Transportation = user.getTransportationFootprint();
                    Housing = user.getHouseFootprint();
                    Food = user.getFoodFootprint();
                    Shopping = user.getConsumptionFootprint();
                    totalEmissions = user.getTotalFootprint();
                    userInformation = user.getUserInformation();

                    PieChart mPieChart = findViewById(R.id.piechart);
                    PieGraphController pieChartController = new PieGraphController(mPieChart);
                    pieChartController.SetChart((float)Transportation,(float) Housing, (float)Food, (float)Shopping);

                    totalEmission.setText("You have emitted "+totalEmissions+"kg of CO2 this year.");
                    LineChartController lineChartController = new LineChartController(mCubicValueLineChart);
                    SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());
                    Calendar calendar = Calendar.getInstance(); // Current date

                    for (int i = 0; i < 3; i++) {
                        String todayDate = dateFormat.format(calendar.getTime());
                        dateArr.add(todayDate);
                        if (userInformation.get(todayDate) == null){
                            Log.d("s", "noData");
                        }
                        else {
                            emissionArr.add(userInformation.get(todayDate).getDailyEmission());
                            Log.d("ss", String.valueOf(userInformation.get(todayDate).getDailyEmission()));
                            calendar.add(Calendar.DATE, +1);
                        }
                    }
                    lineChartController.setChart(emissionArr,dateArr);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EcoGauge","loadPost:onCancelled");
            }
        });


    }

}