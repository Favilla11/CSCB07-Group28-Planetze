package com.example.cscb07_project;

import java.util.regex.Pattern;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.util.Log;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ResetPasswordActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private EditText fullName, resetEmail, signUpPassword, confirmPassword;
    private Button btm;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        resetEmail = findViewById(R.id.ResetPasswordCredentialEmail);
        btm = findViewById(R.id.ResetPasswordButton);

        btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        ImageButton backButton = findViewById(R.id.resetBackButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //replace with EcoTrackerMain
                Intent intent = new Intent(ResetPasswordActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        RelativeLayout layout = findViewById(R.id.main);
        ColorDrawable background = new ColorDrawable(Color.parseColor("green"));
        layout.setBackground(background);
        backButton.setBackground(background);
        ObjectAnimator gradiant = ObjectAnimator.ofObject( background, "color", new ArgbEvaluator(),
                Color.parseColor("#3F51B5"), Color.parseColor("#009999"));
        gradiant.setDuration(3000);
        gradiant.start();

    }

    private void reset() {

        String email = resetEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(ResetPasswordActivity.this, "Please Fill Out All Fields", Toast.LENGTH_SHORT).show();
        } else {

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("ResetPasswordActivity", "Email sent.");
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}