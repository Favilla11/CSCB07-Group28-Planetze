package com.example.cscb07_project;

//import android.util.Patterns;
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

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText fullName, signUpEmail, signUpPassword, confirmPassword;
    private Button signUpButton;
    private TextView redictLogIn;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.CredentialEmail);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.logInPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.logInButton);
        redictLogIn = findViewById(R.id.redirectToSignUp);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        redictLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void signUp(){

        String user = fullName.getText().toString().trim();
        String email = signUpEmail.getText().toString().trim();
        String pass = signUpPassword.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();


        if(user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please Fill Out All Fields", Toast.LENGTH_SHORT).show();
        }
        if(!EMAIL_PATTERN.matcher(email).matches()){
            Toast.makeText(SignUpActivity.this, "Invalid Email Address.", Toast.LENGTH_SHORT).show();
        }
        if(!PASSWORD_PATTERN.matcher(pass).matches()){
            Toast.makeText(SignUpActivity.this, "Must Be A Strong Password", Toast.LENGTH_SHORT).show();
        }
        if(!pass.equals(confirmPass)){
            Toast.makeText(SignUpActivity.this, "Password Does Not Match.", Toast.LENGTH_SHORT).show();
        }
        else{

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, task-> {

                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(SignUpActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        System.out.println("This Email is already registered.");
                    } catch (Exception e) {
                        System.out.println("Registration failed");
                    }
                }
            });
         }
    }


}