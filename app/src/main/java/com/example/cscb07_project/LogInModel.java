package com.example.cscb07_project;

import android.content.Intent;
import java.util.regex.Pattern;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class LogInModel {

    private FirebaseAuth mAuth;

    public LogInModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password, OnLoginFinishedListener listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("MyApp", "login successful");
                listener.onSuccess();
            } else {
                Log.d("MyApp", "Login failed");
                listener.onError(task.getException().getMessage());
            }
        });
    }

    public interface OnLoginFinishedListener {
        void onSuccess();
        void onError(String error);
    }
}
