package com.example.cscb07_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public interface LogInView {
    void showProgress();
    void hideProgress();
    void setEmailError(String error);
    void setPasswordError(String error);
    void navigateToHome();
}