package com.example.cscb07_project;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private Map<String, List<Activity>> activitiesMap;
    private List<Activity> currentDateActivities;
    private ActivityAdapter activityAdapter;
    private String selectedDate;

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

        LinearLayout inputSection = findViewById(R.id.input_section);
        TextView inputLabel = findViewById(R.id.input_label);
        EditText inputField = findViewById(R.id.input_field);

        // Define categories, subcategories, and nested options
        HashMap<String, String[]> subcategories = new HashMap<>();
        subcategories.put("Transportation", new String[]{"Drive Personal Vehicle", "Public Transportation", "Cycling or Walking", "Flight"});
        subcategories.put("Food", new String[]{"Meal"});
        subcategories.put("Consumption", new String[]{"Buy New Clothes", "Buy Electronics", "Other Purchases", "Energy Bills"});

        HashMap<String, String[]> nestedOptions = new HashMap<>();
        nestedOptions.put("Public Transportation", new String[]{"Bus", "Train", "Subway"});
        nestedOptions.put("Flight", new String[]{"Short-Haul (<1,500 km)", "Long-Haul (>1,500 km)"});
        nestedOptions.put("Meal", new String[]{"Beef", "Pork", "Chicken", "Fish", "Plant-Based"});

        // Populate first spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subcategories.keySet().toArray(new String[0]));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Handle category selection
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

        // Handle subcategory selection
        subcategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubcategory = (String) parent.getItemAtPosition(position);

                if (nestedOptions.containsKey(selectedSubcategory)) {
                    // Populate third spinner if nested options exist
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

                // Dynamically set EditText hint based on subcategory
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
                    case "Meal":
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

        // Handle nested spinner selection (optional additional logic can go here)

        activitiesMap = new HashMap<>();
        currentDateActivities = new ArrayList<>();
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
        updateRecyclerView();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            updateRecyclerView();
        });

        addActivityButton.setOnClickListener(v -> {
            // Retrieve selected values
            String category = (String) categorySpinner.getSelectedItem();
            String subcategory = (String) subcategorySpinner.getSelectedItem();
            String nestedOption = nestedSpinner.getVisibility() == View.VISIBLE ? (String) nestedSpinner.getSelectedItem() : null;
            String userInput = inputField.getText().toString().trim();

            // Validate input
            if (category == null || subcategory == null || userInput.isEmpty()) {
                Toast.makeText(this, "Please complete all fields before adding the activity.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build activity description
            StringBuilder activityDescription = new StringBuilder();
            activityDescription.append(subcategory).append("\n");
            if (nestedOption != null) {
                activityDescription.append("Detail: ").append(nestedOption).append("\n");
            }
            activityDescription.append("Input: ").append(userInput);

            // Create new activity
            if (!activitiesMap.containsKey(selectedDate)) {
                activitiesMap.put(selectedDate, new ArrayList<>());
            }
            activitiesMap.get(selectedDate).add(new Activity(selectedDate, activityDescription.toString()));

            // Update RecyclerView
            updateRecyclerView();

            // Clear input fields
            inputField.setText("");
            nestedSpinner.setSelection(0);
            subcategorySpinner.setSelection(0);
            categorySpinner.setSelection(0);

            Toast.makeText(this, "Activity Added!", Toast.LENGTH_SHORT).show();
        });

    }

    private void showAddActivityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Activity");

        final EditText input = new EditText(this);
        input.setHint("Activity Description");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String description = input.getText().toString().trim();
            if (!description.isEmpty()) {
                if (!activitiesMap.containsKey(selectedDate)) {
                    activitiesMap.put(selectedDate, new ArrayList<>());
                }
                activitiesMap.get(selectedDate).add(new Activity(selectedDate, description));
                updateRecyclerView();
                Toast.makeText(this, "Activity Added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Activity description cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
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
                activity.setDescription(newDescription);
                activityAdapter.notifyItemChanged(position);
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
        currentDateActivities.remove(position);
        if (activitiesMap.containsKey(selectedDate)) {
            activitiesMap.get(selectedDate).remove(activity);

            if (activitiesMap.get(selectedDate).isEmpty()) {
                activitiesMap.remove(selectedDate);
            }
        }
        activityAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Activity Deleted!", Toast.LENGTH_SHORT).show();
    }

    private String getFormattedDate(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(timeInMillis));
    }
}


