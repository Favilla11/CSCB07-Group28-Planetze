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

public class LogInPresenter {

    private LogInView view;
    LogInModel model;

    public LogInPresenter(LogInView view) {
        this.view = view;
        this.model = new LogInModel();
    }

    public void validateCredentials(String email, String password) {
        if (email.isEmpty()) {
            view.setEmailError("Email cannot be empty");
            return;
        }
        if (password.isEmpty()) {
            view.setPasswordError("Password cannot be empty");
            return;
        }

        view.showProgress();
        model.login(email, password, new LogInModel.OnLoginFinishedListener() {
            @Override
            public void onSuccess() {
                view.hideProgress();
                view.navigateToHome();
            }

            @Override
            public void onError(String error) {
                view.hideProgress();
                view.setEmailError(error); // or show a general error
            }
        });
    }
}
