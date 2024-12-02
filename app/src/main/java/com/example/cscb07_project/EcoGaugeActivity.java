package com.example.cscb07_project;

import android.os.Bundle;
import android.util.Log;
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

public class EcoGaugeActivity extends AppCompatActivity {

    private double Transportation, Housing,  Food, Shopping;
    private double totalEmission;
    private double yearlyEmission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eco_gauge);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EcoGauge","loadPost:onCancelled");
            }
        });

        ValueLineChart mCubicValueLineChart = findViewById(R.id.cubiclinechart);
        LineChartController lineChartController=new LineChartController(mCubicValueLineChart);
        lineChartController.SetChart();

        PieChart mPieChart = findViewById(R.id.piechart);
        PieGraphController pieChartController=new PieGraphController(mPieChart);
        PieGraphController.SetChart(Transportation, Housing, Food, Shopping);

        TextView totalEmission=findViewById(R.id.TotalEmission);
        // TODO: get value from firebase
        totalEmission.setText("You have emitted "+"kg of CO2 this year.");

        // TODO: get value from firebase
        TextView globalComparison=findViewById(R.id.GlobalComparison);
        globalComparison.setText("Your carbon emission is of "+"% in comparison to global average.");


    }

}