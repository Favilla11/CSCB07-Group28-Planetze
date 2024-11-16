package com.example.cscb07_project;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //background radiation
        RelativeLayout layout = findViewById(R.id.main);
        ColorDrawable background = new ColorDrawable(Color.parseColor("green"));
        layout.setBackground(background);
        ObjectAnimator gradiant = ObjectAnimator.ofObject( background, "color", new ArgbEvaluator(),
                Color.parseColor("#3F51B5"), Color.parseColor("#009999"));
        gradiant.setDuration(3000);
        gradiant.start();

        //slogan Animation
        TextView slogan1 = findViewById(R.id.slogan1);
        TextView slogan2 = findViewById(R.id.slogan2);
        TextView slogan3 = findViewById(R.id.slogan3);
        AnimationSet animation = new AnimationSet(true);
        AlphaAnimation fade = new AlphaAnimation(0.0f, 1.0f);
        fade.setDuration(1800);
        fade.setStartOffset(400);
        fade.setFillAfter(true);

        TranslateAnimation slide = new TranslateAnimation(0,0,1000,0);
        slide.setDuration(1800);
        slide.setStartOffset(400);

        animation.addAnimation(fade);
        animation.addAnimation(slide);
        slogan1.startAnimation(animation);
        slogan2.startAnimation(animation);
        slogan3.startAnimation(animation);



        // onStart()
        //    FirebaseUser currentUser = mAuth.getCurrentUser();
        //    updateUI(currentUser);
    }
}