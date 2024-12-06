package com.example.cscb07_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button addActivityButton;
    private RecyclerView activitiesRecyclerView;
    private TextView emissionTextView;

    private Map<String, List<Activity>> activitiesMap;
    private Map<String, Information> userInformation;
    private List<Activity> currentDateActivities;
    private ActivityAdapter activityAdapter;
    private String selectedDate;
    private List<Habit> habitList;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;

    public double dailyEmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        addActivityButton = findViewById(R.id.addActivityButton);
        activitiesRecyclerView = findViewById(R.id.activitiesRecyclerView);
        Spinner categorySpinner = findViewById(R.id.category_spinner);
        Spinner subcategorySpinner = findViewById(R.id.subcategory_spinner);
        Spinner nestedSpinner = findViewById(R.id.nested_spinner);

        TextView subcategoryLabel = findViewById(R.id.subcategory_label);
        TextView nestedLabel = findViewById(R.id.nested_label);
        emissionTextView = findViewById(R.id.dailyEmissionView);

        LinearLayout inputSection = findViewById(R.id.input_section);
        TextView inputLabel = findViewById(R.id.input_label);
        EditText inputField = findViewById(R.id.input_field);
        userInformation = new HashMap<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        currentDateActivities = new ArrayList<>();


        habitList = new ArrayList<>();



        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });


        dailyEmission = 0;
        List<String> categories = new ArrayList<>();
        categories.add("------Select Category------");
        categories.add("Transportation");
        categories.add("Food");
        categories.add("Consumption");

        HashMap<String, String[]> subcategories = new HashMap<>();
        subcategories.put("Transportation", new String[]{"Drive Personal Vehicle", "Public Transportation", "Cycling or Walking", "Flight"});
        subcategories.put("Food", new String[]{"Beef", "Pork", "Chicken", "Fish", "Plant-Based"});
        subcategories.put("Consumption", new String[]{"Buy New Clothes", "Buy Electronics", "Other Purchases", "Energy Bills"});
        subcategories.put("------Select Category------", new String[]{});

        HashMap<String, String[]> nestedOptions = new HashMap<>();
        nestedOptions.put("Public Transportation", new String[]{"Bus", "Train", "Subway"});
        nestedOptions.put("Flight", new String[]{"Short-Haul (<1,500 km)", "Long-Haul (>1,500 km)"});
        nestedOptions.put("Energy Bills", new String[]{"Water", "Gas", "Electricity"});

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(0);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);

                // Populate second spinner
                ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(CalendarActivity.this, android.R.layout.simple_spinner_item, subcategories.get(selectedCategory));
                subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subcategorySpinner.setAdapter(subcategoryAdapter);

                subcategoryLabel.setVisibility(View.VISIBLE);
                subcategorySpinner.setVisibility(View.VISIBLE);
                nestedSpinner.setVisibility(View.GONE);
                inputSection.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        subcategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubcategory = (String) parent.getItemAtPosition(position);


                if (nestedOptions.containsKey(selectedSubcategory)) {
                    ArrayAdapter<String> nestedAdapter = new ArrayAdapter<>(CalendarActivity.this, android.R.layout.simple_spinner_item, nestedOptions.get(selectedSubcategory));
                    nestedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    nestedSpinner.setAdapter(nestedAdapter);

                    nestedLabel.setVisibility(View.VISIBLE);
                    nestedSpinner.setVisibility(View.VISIBLE);
                } else {
                    nestedLabel.setVisibility(View.GONE);
                    nestedSpinner.setVisibility(View.GONE);
                }

                inputSection.setVisibility(View.VISIBLE);
                inputLabel.setText("Enter details for: " + selectedSubcategory);

                switch (selectedSubcategory) {
                    case "Drive Personal Vehicle":
                        inputField.setHint("Enter distance driven (km or miles)");
                        break;
                    case "Public Transportation":
                        inputField.setHint("Enter time spent (hours)");
                        break;
                    case "Cycling or Walking":
                        inputField.setHint("Enter distance cycled or walked (km or miles)");
                        break;
                    case "Flight":
                        inputField.setHint("Enter number of flights today");
                        break;
                    case "Beef":
                    case "Pork":
                    case "Chicken":
                    case "Fish":
                    case "Plant-Base":
                        inputField.setHint("Enter number of servings");
                        break;
                    case "Buy New Clothes":
                        inputField.setHint("Enter number of clothing items purchased");
                        break;
                    case "Buy Electronics":
                        inputField.setHint("Enter number of devices purchased");
                        break;
                    case "Other Purchases":
                        inputField.setHint("Enter type and quantity of purchases");
                        break;
                    case "Energy Bills":
                        inputField.setHint("Enter bill amount (e.g., $150)");
                        break;
                    default:
                        inputField.setHint("Enter details");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        activitiesMap = new HashMap<>();
        activityAdapter = new ActivityAdapter(currentDateActivities, new ActivityAdapter.OnActivityClickListener() {
            @Override
            public void onEditClick(Activity activity, int position) {
                showEditActivityDialog(activity, position);
            }

            @Override
            public void onDeleteClick(Activity activity, int position) {
                deleteActivity(activity, position);
            }
        });

        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activitiesRecyclerView.setAdapter(activityAdapter);

        selectedDate = getFormattedDate(calendarView.getDate());
        Log.d("datee", selectedDate);
        updateRecyclerView();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
//            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            selectedDate = String.format("%d-%02d-%d", year, month + 1, dayOfMonth);
            updateRecyclerView();

            dailyEmission = calculateDailyEmission(selectedDate);
            emissionTextView.setText("Total Emission: " + dailyEmission + "kg CO₂");

        });
//        getUserHabits();
        DatabaseReference habitListRef = databaseReference.child(currentUser.getUid()).child("habitList");
        habitListRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                    String action = habitSnapshot.child("act").getValue(String.class);
                    String category = habitSnapshot.child("category").getValue(String.class);
                    Double progress = habitSnapshot.child("progress").getValue(Double.class);
                    Double impact = habitSnapshot.child("impact").getValue(Double.class);
                    Double frequency = habitSnapshot.child("frequency").getValue(Double.class);
                    if (impact == null) impact = 0.0;
                    if (progress == null) progress = 0.0;
                    if (frequency == null) frequency = 0.0;
                    habitList.add(new Habit(category, progress,action,impact, frequency));
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching habits: " + error.getMessage());
            }

        });
        addActivityButton.setOnClickListener(v -> {
            // Retrieve selected values
            String category = (String) categorySpinner.getSelectedItem();
            if (category.equals("------Select Category------")){
                Toast.makeText(this, "Please complete all fields before adding the activity.", Toast.LENGTH_SHORT).show();
                return;
            }
            String subcategory = (String) subcategorySpinner.getSelectedItem();
            String nestedOption = nestedSpinner.getVisibility() == View.VISIBLE ? (String) nestedSpinner.getSelectedItem() : null;
            String userInput = inputField.getText().toString().trim();
            String hint = "";
            String detail = "";



            switch (subcategory){
                case "Drive Personal Vehicle":
                case "Cycling or Walking":
                    hint = "Distance:";
                    break;
                case "Beef":
                case "Pork":
                case "Chicken":
                case "Fish":
                case "Plant-Based":
                    hint = "Number Consume:";
                    break;
                case "Buy New Clothes":
                case "Buy Electronics":
                case "Other Purchases":
                    hint = "Number Purchase:";
                    break;
                case "Public Transportation":
                case "Flight":
                    hint = "Duration:";
                    detail = "Type:";
                    break;

                case "Energy Bills":
                    hint = "Amount";
                    detail = "Type:";
                    break;
            }

            if (userInput.isEmpty()) {
                Toast.makeText(this, "Please complete all fields before adding the activity.", Toast.LENGTH_SHORT).show();
                return;
            }
            double number = parseToNumber(userInput);
            if (number == 0.0){
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                return;
            }
            double emission = calculateEmission(category, subcategory, nestedOption, userInput);
            Log.d("DailyEmission", "Emission: " + emission);

            StringBuilder activityDescription = new StringBuilder();
            activityDescription.append(subcategory).append("\n");
            if (nestedOption != null) {
                activityDescription.append(detail).append(nestedOption).append("\n");
            }
            activityDescription.append(hint).append(userInput);

            // Create new activity
            if (!activitiesMap.containsKey(selectedDate)) {
                activitiesMap.put(selectedDate, new ArrayList<>());
            }
            activitiesMap.get(selectedDate).add(new Activity(selectedDate, activityDescription.toString(),category, subcategory, emission));


            habitListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        boolean habitUpdated = false;

                        // Iterate through all habits in the list
                        for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                            Habit habit = habitSnapshot.getValue(Habit.class);

                            // Check if the habit matches the given category
                            if (habit != null && habit.getCategory().equals(subcategory)) {
                                habit.updateProgress(1.0); // Update progress locally

                                // Update habit in Firebase using its key
                                habitSnapshot.getRef().setValue(habit)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("FirebaseUpdate", "Habit progress updated successfully.");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("FirebaseUpdate", "Failed to update habit progress", e);
                                        });

                                habitUpdated = true;
                                break; // Exit the loop after updating the habit
                            }
                        }

                        if (!habitUpdated) {
                            Log.d("FirebaseUpdate", "No habit found matching the given category.");
                        }
                    } else {
                        Log.d("FirebaseUpdate", "Habit list is empty.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseUpdate", "Error fetching habit list: " + error.getMessage());
                }
            });


            updateRecyclerView();

            inputField.setText("");

            dailyEmission = calculateDailyEmission(selectedDate);
            userInformation.put(selectedDate, new Information(currentDateActivities, dailyEmission));
            Log.d("userInfo", String.valueOf(userInformation.get(selectedDate).getActivityList().get(0)));
            saveData(userInformation);

            emissionTextView.setText(String.format("Total Emission: %.2f kg CO₂", dailyEmission));

            Toast.makeText(this, "Activity Added!", Toast.LENGTH_SHORT).show();
        });
    }


    private void showEditActivityDialog(Activity activity, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Activity");

        final EditText input = new EditText(this);
        input.setText(activity.getDescription());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newDescription = input.getText().toString().trim();
            if (!newDescription.isEmpty()) {
                double oldEmissions = activity.getEmission();
                activity.setDescription(newDescription);
                double newEmission = calculateEmission(activity.getCategory(), activity.getSubCategory(), null, activity.getDescription());
                activity.setEmission(newEmission);
                dailyEmission -= oldEmissions;
                dailyEmission += newEmission;

                activityAdapter.notifyItemChanged(position);
                List<Activity> activitiesForDate = activitiesMap.get(activity.getDate());
                activitiesForDate.set(position, activity);
                activitiesMap.put(activity.getDate(), activitiesForDate);

                userInformation.put(selectedDate, new Information(activitiesMap.get(activity.getDate()), dailyEmission));
                saveData(userInformation);

                emissionTextView.setText(String.format("Total Emission: %.2f kg CO₂", dailyEmission));

                Toast.makeText(this, "Activity Updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateRecyclerView() {
        currentDateActivities.clear();
        if (activitiesMap.containsKey(selectedDate)) {
            currentDateActivities.addAll(activitiesMap.get(selectedDate));
        }
        activityAdapter.notifyDataSetChanged();
    }

    private void deleteActivity(Activity activity, int position) {
        double emissionToRemove = currentDateActivities.get(position).getEmission();
        currentDateActivities.remove(position);


        if (activitiesMap.containsKey(selectedDate)) {
            activitiesMap.get(selectedDate).remove(activity);

            if (activitiesMap.get(selectedDate).isEmpty()) {
                activitiesMap.remove(selectedDate);
            }
        }

        dailyEmission = calculateDailyEmission(selectedDate);
        activityAdapter.notifyItemRemoved(position);
        userInformation.put(selectedDate, new Information(currentDateActivities, dailyEmission));
        saveData(userInformation);
        emissionTextView.setText(String.format("Total Emission: %.2f kg CO₂", dailyEmission));
        Toast.makeText(this, "Activity Deleted!", Toast.LENGTH_SHORT).show();
    }

    private String getFormattedDate(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());
        return sdf.format(new Date(timeInMillis));
    }
    public double calculateEmission(String mainType, String secondType, @Nullable String nestedOption, String userInput){
        String type = mainType.toLowerCase();
        String subType = secondType.toLowerCase();
        String detail = null;
        if (nestedOption != null) {
            detail = nestedOption.toLowerCase();
        }
        double input = parseToNumber(userInput);
        double result = 0;
        if (type.equals("transportation")) {
            switch (subType) {
                case "drive personal vehicle":
                    result = logDriving(input);
                    break;
                case "public transportation":
                    result = logPublicTransport(input, detail);
                    break;
                case "cycling or walking":
                    result = logCyclingWalking(input);
                    break;
                case "flight":
                    result = logFlights(input, detail);
                    break;
                default:
                    Toast.makeText(this, "Unknown transport type", Toast.LENGTH_SHORT).show();
            }
        } else if (type.equals("food")) {
            switch (subType) {
                case "beef":
                    result = logBeef(input);
                    break;
                case "pork":
                    result = logPork(input);
                    break;
                case "chicken":
                    result = logChicken(input);
                    break;
                case "fish":
                    result = logFish(input);
                    break;
                case "plant-based":
                    result = logPlant(input);
                    break;
                default:
                    Toast.makeText(this, "Unknown transport type", Toast.LENGTH_SHORT).show();
            }
        } else if (type.equals("consumption")) {
            switch (subType) {
                case "buy new clothes":
                    result = logBuyNewClothes(input);
                    break;
                case "buy electronics":
                    result = logBuyElectronics(input);
                    break;
                case "other purchases":
                    result = logOtherPurchases(input);
                    break;
                case "energy bills":
                    result = logEnergyBills(input, detail);
                    break;
                default:
                    Toast.makeText(this, "Unknown transport type", Toast.LENGTH_SHORT).show();
            }

        }
        return result;

    }
    private double logDriving(double input) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter distance driven", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double emissionsPerKm = 0.24;

        return input * emissionsPerKm;
    }

    private double logPublicTransport(double input, String detail) {
        double emissionsPerKm = 0.2;
        if (detail != null) {
            if (detail.equals("bus")) {
                emissionsPerKm = 0.15;
            } else if (detail.equals("train")) {
                emissionsPerKm = 0.1;
            } else if (detail.equals("subway")) {
                emissionsPerKm = 0.05;
            }
        }
        if (input == 0.0) {
            Toast.makeText(this, "Please enter time spent on public transport", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return input * emissionsPerKm;
    }

    private double logCyclingWalking(double input) {
        return input * 0;
    }

    private double logFlights(double input, String detail) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter number of flights", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double emissionsPerFlight = 0.5;
        if (detail != null) {
            if (detail.equals("long-haul")) {
                emissionsPerFlight = 2.0;
            }
        }

        return input * emissionsPerFlight;
    }

    private double logBeef(double input) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return input * 0.6;
    }

    private double logPork(double input) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return input * 0.3;
    }

    private double logChicken(double input) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return input * 0.2;
    }

    private double logFish(double input) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return input * 0.1;
    }

    private double logPlant(double input) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return input * 0.25;
    }

    private double logBuyNewClothes(double input) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter number of clothing iterms purchased", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return input * 0.1;
    }

    private double logBuyElectronics(double input) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter number of devices purchased", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return input * 0.1;
    }

    private double logOtherPurchases(double input) {
        if (input == 0.0) {
            Toast.makeText(this, "Please enter number of other purchases", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return input * 0.5;
    }

    private double logEnergyBills(double input, String detail) {
        double factor = 0.5;
        if (detail != null) {
            if (detail.equals("Electricity")) {
                factor = 1;
            } else if (detail.equals("Gas")) {
                factor = 0.8;
            } else if (detail.equals("Water")) {
                factor = 0.5;
            }
        }
        return input * factor;
    }


    public double parseToNumber(String input) {
        String numericString = input.replaceAll("[^\\d.]", "");

        try {
            return Double.parseDouble(numericString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            return 0.0;
        }
    }
    public double calculateDailyEmission(String date){
        double result = 0;
        if (activitiesMap.get(date) == null){
            return 0;
        }
        for(Activity activity : currentDateActivities) {
            result += activity.getEmission();
        }
        return result;
    }
    public void saveData(Map<String, Information> map){
        databaseReference.child(currentUser.getUid()).child("userInformation").setValue(map).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d("mess", "saved");
            }
            else{
                Log.d("mess", "not saved");

            }
        });
    }
}



