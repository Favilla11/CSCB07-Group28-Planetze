package com.example.cscb07_project;

import java.io.IOException;
import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.ArrayAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class QuestionnaireActivity extends AppCompatActivity {
    private int currentQuestionIndex = 0;
    private ArrayList<Question> questionList;
    private RadioGroup radioGroup;
    private Button nextButton;
    private TextView questionText;
    private HashMap<Integer, String> storedAnswer = new HashMap<>();
    private HashMap<String, Integer> countryValue = new HashMap<>();

    private Spinner beefSpinner, porkSpinner, chickenSpinner, fishSpinner, regionSpinner;

    private ArrayList<String> regions = new ArrayList<>();
    private HashMap<String, Spinner> spinnerMap;

    private boolean isAddressSelected = false;

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
        regionSpinner = findViewById(R.id.regionSpinner);
        questionList = QuestionBank.getQuestions();
        questionText = findViewById(R.id.questionText);
        radioGroup = findViewById(R.id.radioGroup);

        nextButton = findViewById(R.id.nextButton);
        beefSpinner = findViewById(R.id.beefSpinner);
        porkSpinner = findViewById(R.id.porkSpinner);
        chickenSpinner = findViewById(R.id.chickenSpinner);
        fishSpinner = findViewById(R.id.fishSpinner);

        spinnerMap = new HashMap<>();
        spinnerMap.put("Beef", beefSpinner);
        spinnerMap.put("Pork", porkSpinner);
        spinnerMap.put("Chicken", chickenSpinner);
        spinnerMap.put("Fish/Seafood", fishSpinner);

        ArrayAdapter<String> meatFrequencyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"});
        meatFrequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        beefSpinner.setAdapter(meatFrequencyAdapter);
        porkSpinner.setAdapter(meatFrequencyAdapter);
        chickenSpinner.setAdapter(meatFrequencyAdapter);
        fishSpinner.setAdapter(meatFrequencyAdapter);

        try {
            initRegions();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load region data", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                regions);
        meatFrequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);

        setupOptionListeners();
        displayRegionSelection();
        nextButton.setOnClickListener(view -> next());
    }

    private void displayRegionSelection() {
        questionText.setText("Please select your region:");
        radioGroup.removeAllViews();
        regionSpinner.setVisibility(View.VISIBLE);
        findViewById(R.id.regionLabel).setVisibility(View.VISIBLE);
        findViewById(R.id.regionSpinner).setVisibility(View.VISIBLE);
    }

    private void setupOptionListeners() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = findViewById(checkedId);
            String selectedMeat = checkedRadioButton.getText().toString();
            updateSpinnerVisibility(selectedMeat);
        });
    }

    private void updateSpinnerVisibility(String selectedMeat) {
        for (Map.Entry<String, Spinner> entry : spinnerMap.entrySet()) {
            if (entry.getKey().equals(selectedMeat)) {
                entry.getValue().setVisibility(View.VISIBLE);
            } else {
                entry.getValue().setVisibility(View.GONE);
            }
        }
    }

    private void displayQuestion() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        String displayText = currentQuestion.getQuestionId() + " " + currentQuestion.getQuestionText();
        questionText.setText(displayText);
        radioGroup.removeAllViews();
        findViewById(R.id.regionLabel).setVisibility(View.GONE);
        findViewById(R.id.regionSpinner).setVisibility(View.GONE);
        if (currentQuestion.getQuestionId() == 9) {
            radioGroup.setVisibility(View.GONE);
            findViewById(R.id.beefLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.beefSpinner).setVisibility(View.VISIBLE);
            findViewById(R.id.porkLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.porkSpinner).setVisibility(View.VISIBLE);
            findViewById(R.id.chickenLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.chickenSpinner).setVisibility(View.VISIBLE);
            findViewById(R.id.fishLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.fishSpinner).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.beefLabel).setVisibility(View.GONE);
            findViewById(R.id.beefSpinner).setVisibility(View.GONE);
            findViewById(R.id.porkLabel).setVisibility(View.GONE);
            findViewById(R.id.porkSpinner).setVisibility(View.GONE);
            findViewById(R.id.chickenLabel).setVisibility(View.GONE);
            findViewById(R.id.chickenSpinner).setVisibility(View.GONE);
            findViewById(R.id.fishLabel).setVisibility(View.GONE);
            findViewById(R.id.fishSpinner).setVisibility(View.GONE);

            radioGroup.setVisibility(View.VISIBLE);

            String[] options = currentQuestion.getOption();
            for (String option : options) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                radioGroup.addView(radioButton);
            }
        }
    }


    private void next() {
        if (!isAddressSelected) {
            String selectedRegion = regionSpinner.getSelectedItem().toString();
            storedAnswer.put(0, selectedRegion);
            isAddressSelected = true;

            currentQuestionIndex = 0;
            displayQuestion();
        } else{
            Question currentQuestion = questionList.get(currentQuestionIndex);
            if (currentQuestion.getQuestionId() == 9) {
                String beefFrequency = beefSpinner.getSelectedItem().toString();
                String porkFrequency = porkSpinner.getSelectedItem().toString();
                String chickenFrequency = chickenSpinner.getSelectedItem().toString();
                String fishFrequency = fishSpinner.getSelectedItem().toString();

                String answer = "Beef: " + beefFrequency + ", Pork: " + porkFrequency +
                        ", Chicken: " + chickenFrequency + ", Fish/Seafood: " + fishFrequency;

                storedAnswer.put(currentQuestion.getQuestionId(), answer);
                currentQuestionIndex = getNextQuestionIndex(currentQuestionIndex, answer);
                displayQuestion();
            } else {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(QuestionnaireActivity.this, "Please Select A Choice", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedOption = findViewById(selectedId);
                String answer = selectedOption.getText().toString();
                storedAnswer.put(currentQuestion.getQuestionId(), answer);
                currentQuestionIndex = getNextQuestionIndex(currentQuestionIndex, answer);
                if (currentQuestionIndex < questionList.size()) {
                    displayQuestion();
                } else {
                    questionText.setText("Thank you for completing the questionnaire!");
                    nextButton.setText("See the result ->");
                    radioGroup.setVisibility(View.GONE);
//
//                    Intent intent = new Intent(QuestionnaireActivity.this, DisplayResultActivity.class);
//                    intent.putExtra("userResponse", storedAnswer);
//                    toResultPage();
                }
            }
        }
    }

    private int getNextQuestionIndex(int index, String answer) {
        if (questionList.get(index).getQuestionId() == 1) {
            return answer.equals("No") ? 3 : index + 1;
        }
        if (questionList.get(index).getQuestionId() == 8) {
            return answer.equals("Meat-based (eat all types of animal products)") ? index + 1 : 9;
        }
        return index + 1;
    }
    private void initRegions() throws IOException {

        try (InputStream inputStream = getAssets().open("D:\\CSCB07-Group28-Planetze\\app\\build\\intermediates\\assets\\Global_Averages.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {

                System.out.println(line);
                String[] parts = line.split("\t");
                if (parts.length > 0) {
                    regions.add(parts[0]);
                }
            }
        }

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, regions);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);
    }
//    private void toResultPage() {
//        Intent intent = new Intent(QuestionnaireActivity.this, DisplayResultActivity.class);
//        startActivity(intent);
//        finish();
//    }
}




