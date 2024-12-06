package com.example.cscb07_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.eazegraph.lib.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomePageActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView welcomeText = findViewById(R.id.welcomeText);
        Button monitorBtn = findViewById(R.id.monitor_btn);
        Button habitSuggestionBtn = findViewById(R.id.habitSuggestion_btn);
        Button ecoGaugeBtn = findViewById(R.id.ecoGauge_btn);
        Button questionnaireBtn = findViewById(R.id.questionnaire_btn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        ref.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
                if (user != null) {
                    SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());
                    Calendar calendar = Calendar.getInstance();
                    String date = dateFormat.format(calendar.getTime());
                    String userName = user.getUserName();

                    welcomeText.setText("Welcome Back! " + userName);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EcoGauge","loadPost:onCancelled");
            }
        });
        ecoGaugeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, EcoGaugeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        monitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });
        habitSuggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, HabitSuggestionActivity.class);
                startActivity(intent);
                finish();
            }
        });
        questionnaireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, QuestionnaireActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}