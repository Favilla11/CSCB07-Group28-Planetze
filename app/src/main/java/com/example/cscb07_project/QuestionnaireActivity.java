package com.example.cscb07_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionnaireActivity extends AppCompatActivity {

    // Store the current question index
    private int currentQuestionIndex = 0;

    // Array of questions
    private String[] questions = {
            "Do you own or regularly use a car?",
            "What type of car do you drive?",
            "How many kilometers/miles do you drive per year?",
            "How often do you use public transportation?",
            "How much time do you spend on public transport per week?",
            "How many short-haul flights have you taken in the past year?",
            "How many long-haul flights have you taken in the past year?"
    };

    // 2D Array to hold the options for each question
    private String[][] options = {
            {"No", "Yes"},
            {"Gasoline", "Diesel", "Hybrid", "Electric", "I don't know"},
            {"Up to 5,000 km", "5,000–10,000 km", "10,000–15,000 km", "15,000–20,000 km", "20,000–25,000 km", "More than 25,000 km"},
            {"Never", "Occasionally (1-2 times/week)", "Frequently (3-4 times/week)", "Always (5+ times/week)"},
            {"Under 1 hour", "1-3 hours", "3-5 hours", "5-10 hours", "More than 10 hours"},
            {"None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"},
            {"None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"}
    };

    // Array to store the answers
    private String[] answers = new String[questions.length];

    // Views
    private TextView questionText;
    private RadioGroup radioGroup;  // For the RadioButtons
    private Spinner spinner;  // For selecting from the options like kilometers, hours, etc.
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        // Find the views
        questionText = findViewById(R.id.questionText);
        radioGroup = findViewById(R.id.radioGroup);
        spinner = findViewById(R.id.spinner); // Assume you are using a Spinner for certain questions like kilometers
        nextButton = findViewById(R.id.nextButton);

        // Set the initial question
        questionText.setText(questions[currentQuestionIndex]);
        setupQuestion();

        // Set the listener for the "Next" button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capture the selected answer
                String selectedAnswer = getSelectedAnswer();

                if (selectedAnswer != null) {
                    answers[currentQuestionIndex] = selectedAnswer;
                    moveToNextQuestion();
                } else {
                    Toast.makeText(QuestionnaireActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Get the selected answer from the current question
    private String getSelectedAnswer() {
        // Check for RadioButton answers
        if (radioGroup.getVisibility() == View.VISIBLE) {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                return selectedRadioButton.getText().toString();
            }
        }

        // Check for Spinner answers
        if (spinner.getVisibility() == View.VISIBLE) {
            return spinner.getSelectedItem().toString();
        }

        return null;  // No answer selected
    }

    // Move to the next question
    private void moveToNextQuestion() {
        // Increment the question index and update the question
        currentQuestionIndex++;

        if (currentQuestionIndex < questions.length) {
            questionText.setText(questions[currentQuestionIndex]);
            setupQuestion();  // Update the UI for the new question
        } else {
            Toast.makeText(this, "Thank you for completing the questionnaire!", Toast.LENGTH_SHORT).show();
            // You can proceed to the next activity or show a summary here
        }
    }

    // Set up the question and answers based on the current question
    private void setupQuestion() {
        // Clear previous selections
        radioGroup.clearCheck();  // Clear selected radio button
        radioGroup.removeAllViews();  // Remove all dynamically added radio buttons
        spinner.setVisibility(View.GONE);  // Hide Spinner
        radioGroup.setVisibility(View.GONE);  // Hide RadioGroup

        // Check if the current question is a radio button question
        if (currentQuestionIndex == 0 || currentQuestionIndex == 1 || currentQuestionIndex == 3 ||
                currentQuestionIndex == 4 || currentQuestionIndex == 5 || currentQuestionIndex == 6) {

            radioGroup.setVisibility(View.VISIBLE);  // Show RadioGroup
            addRadioButtons(options[currentQuestionIndex]);  // Dynamically add radio buttons

        } else if (currentQuestionIndex == 2) {  // If it's the kilometers question, use Spinner
            spinner.setVisibility(View.VISIBLE);  // Show Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options[currentQuestionIndex]);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    // Dynamically add radio buttons to the RadioGroup
    private void addRadioButtons(String[] options) {
        for (String option : options) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioGroup.addView(radioButton);  // Add the RadioButton to the RadioGroup
        }
    }
}


