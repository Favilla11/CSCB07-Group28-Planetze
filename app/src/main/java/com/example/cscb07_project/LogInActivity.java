package com.example.cscb07_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity implements LogInView {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private LogInPresenter presenter;
    private TextView redirectToSignUp;
    private TextView forgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.ResetPasswordCredentialEmail);
        passwordEditText = findViewById(R.id.logInPassword);
        loginButton = findViewById(R.id.logInButton);
        redirectToSignUp = findViewById(R.id.redirectToSignUp);
        forgetPassword = findViewById(R.id.ForgetPassword);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });


        presenter = new LogInPresenter(this, new LogInModel()); //

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
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }
}