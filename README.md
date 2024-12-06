Overview

    •    App Name: Planetze
    •    Description: An app to track a user's carbon footprint using their habits from a questiionaire and using them to suggest habits and 
         tracks their progress to help them reduce their carbon emissions
    •    Technologies Used:
    •    Programming Language: Java
    •    Architecture: MVVM/MVP/MVC
    •    Database: Firebase
    •    Other Tools/Libraries: Retrofit, Glide, etc.

Project Structure

    •    Package Organization:
    •    com.example.cscb07_project.app
    •    ui: All UI-related components (Activities, Fragments, Adapters).
    •    data: Data layer (Repositories, API clients, Database).
                    com.google.firebase.database.DataSnapshot;
                    com.google.firebase.database.DatabaseError;
                    com.google.firebase.database.ValueEventListener;
                    com.google.firebase.auth.FirebaseAuth;
                    com.google.firebase.auth.FirebaseUser;
                    com.google.firebase.database.DatabaseReference;
                    com.google.firebase.database.FirebaseDatabase;
    •    model: Habit, habits to suggest to user, String category, double progress, double impact, String act, double frequency
                Question, questions for questionaire, int questionId, String questionText, String category, String[] option
                Activity, daily activities of user, String date, String description, String category, String subCategory, double emission
                Information, all information of user, String date, String description, String category, String subCategory, double emission
                HabitAdapter, adapts habits for view, @NonNull List<Habit> habits
                Calendar
    •    viewmodel: ViewModel classes for the MVVM architecture.
    •    util:      android.animation.ArgbEvaluator;
                    android.animation.ObjectAnimator;
                    android.content.Intent;
                    android.graphics.Color;
                    android.graphics.drawable.ColorDrawable;
                    android.os.Bundle;
                    android.text.TextUtils;
                    android.util.Log;
                    android.view.View;
                    android.widget.AdapterView;
                    android.widget.ArrayAdapter;
                    android.widget.EditText;
                    android.widget.ImageButton;
                    android.widget.ListView;
                    android.widget.SearchView;
                    android.widget.Spinner;
                    android.widget.Toast;

                    androidx.activity.EdgeToEdge;
                    androidx.annotation.NonNull;
                    androidx.appcompat.app.AlertDialog;
                    androidx.appcompat.app.AppCompatActivity;
                    androidx.constraintlayout.widget.ConstraintLayout;
                    androidx.core.graphics.Insets;
                    androidx.core.view.ViewCompat;
                    androidx.core.view.WindowInsetsCompat;
                    androidx.recyclerview.widget.LinearLayoutManager;
                    androidx.recyclerview.widget.RecyclerView;
                    java.text.SimpleDateFormat;
                    java.util.ArrayList;
                    java.util.Calendar;
                    java.util.Date;
                    java.util.Locale;
                    java.util.Map;
    •    service: Background services and notifications.
    •    important Files:
    •    MainActivity.java: Entry point of the app.
    •    AndroidManifest.xml: App configuration and permissions.
         formulas.csv: Data for calculating carbon footprint

Setup Guide

Prerequisites

    •    Android Studio 2024.2.1 Patch 2
    •    Java 17
    •    Required APIs/SDKs Firebase
