package com.example.cscb07_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionnaireActivity extends AppCompatActivity {
    private int currentQuestionIndex = 0;
    private ArrayList<Question> questionList;
    private RadioGroup radioGroup;
    private Button nextButton;
    private TextView questionText;
    private HashMap<Integer, String> storedAnswer = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questionnaire);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        questionList = QuestionBank.getQuestions();
        questionText = findViewById(R.id.questionText);
        radioGroup = findViewById(R.id.radioGroup);
        nextButton = findViewById(R.id.nextButton);

        displayQuestion();

        nextButton.setOnClickListener(view -> next());
    }

    private void displayQuestion() {
        // Get the current question from the list
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestionText());

        // Clear previous options
        radioGroup.removeAllViews();

        // Get the options for the current question and display them
        String[] options = currentQuestion.getOption();
        for (String option : options) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioGroup.addView(radioButton);
        }
    }

    private void next() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(QuestionnaireActivity.this, "Please Select A Choice", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton selectedOption = findViewById(selectedId);
            String answer = selectedOption.getText().toString();
            storedAnswer.put(questionList.get(currentQuestionIndex).getQuestionId(), answer);

            // Get next question index based on the answer
            currentQuestionIndex = getNextQuestionIndex(currentQuestionIndex, answer);

            // Check if there are more questions
            if (currentQuestionIndex < questionList.size()) {
                displayQuestion();
            } else {
                questionText.setText("Thank you for completing the questionnaire!");
                nextButton.setVisibility(View.GONE);  // Hide the "Next" button when done
            }
        }
    }

    private int getNextQuestionIndex(int index, String answer) {
        if (questionList.get(index).getQuestionId() == 1) {
            if (answer.equals("No")) {
                return 3;  // Skip to question 3 if "No"
            }
            return index + 1;
        }

        if (questionList.get(index).getQuestionId() == 8) {
            if (answer.equals("Meat-based (eat all types of animal products)")) {
                return index + 1;
            }
            return 9;  // Go to question 9 if answer is not "Meat-based"
        }

        return index + 1;  // Otherwise just go to the next question
    }
}




