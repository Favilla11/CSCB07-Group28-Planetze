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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference itemRef;
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
        db = FirebaseDatabase.getInstance();

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
        else if(!EMAIL_PATTERN.matcher(email).matches()){
            Toast.makeText(SignUpActivity.this, "Invalid Email Address.", Toast.LENGTH_SHORT).show();
        }
        else if(!PASSWORD_PATTERN.matcher(pass).matches()){
            Toast.makeText(SignUpActivity.this, "Must Be A Strong Password", Toast.LENGTH_SHORT).show();
        }
        else if(!pass.equals(confirmPass)){
            Toast.makeText(SignUpActivity.this, "Password Does Not Match.", Toast.LENGTH_SHORT).show();
        }
        else{

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, task-> {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        firebaseUser.sendEmailVerification().addOnCompleteListener(this, task1-> {
                            if (task1.isSuccessful()) {
                                saveUserToDB(firebaseUser.getUid(), user, email, pass);
                                Toast.makeText(SignUpActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, WelcomePageActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
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
    private void saveUserToDB(String Uid, String userName, String email, String pass){
        itemRef = db.getReference("users");
        User user = new User(userName, email, pass, 0,0,0,0,0, null, null);
        itemRef.child(Uid).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Database", "User data saved successfully");
            } else {
                Log.e("Database", "Failed to save user data", task.getException());
            }
        });
    }


}