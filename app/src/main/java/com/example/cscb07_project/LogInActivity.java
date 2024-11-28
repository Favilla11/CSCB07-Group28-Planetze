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

public class LogInActivity extends AppCompatActivity implements LogInView {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private LogInPresenter presenter;
    private TextView redirectToSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.CredentialEmail);
        passwordEditText = findViewById(R.id.logInPassword);
        loginButton = findViewById(R.id.logInButton);
        redirectToSignUp = findViewById(R.id.redirectToSignUp);

        presenter = new LogInPresenter(this);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            presenter.validateCredentials(email, password);
        });

        redirectToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void showProgress() {
        // Show a progress bar or loading indicator
    }

    @Override
    public void hideProgress() {
        // Hide the progress bar or loading indicator
    }

    @Override
    public void setEmailError(String error) {
        emailEditText.setError(error);
    }

    @Override
    public void setPasswordError(String error) {
        passwordEditText.setError(error);
    }

    @Override
    public void navigateToHome() {
        // TODO: set after Home Activity is implemented
        // Navigate to the home screen
        //Intent intent = new Intent(this, HomeActivity.class);
        //startActivity(intent);
        //finish();
    }
}
