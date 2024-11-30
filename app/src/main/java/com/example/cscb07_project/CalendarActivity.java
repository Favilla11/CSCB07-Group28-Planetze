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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
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

    private Map<String, List<Activity>> activitiesMap;
    private List<Activity> currentDateActivities;
    private ActivityAdapter activityAdapter;
    private String selectedDate;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

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

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        List<Habit> habitList = new ArrayList<>();

        String userId = currentUser.getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve User data
                    User user = dataSnapshot.getValue(User.class);

                    for (DataSnapshot habitSnapshot : dataSnapshot.child("habitlist").getChildren()) {
                        Habit habit = habitSnapshot.getValue(Habit.class);
                        habitList.add(habit);
                    }

                    user.habitlist = habitList;

                    Log.d("Firebase", "User: " + user.username);
                    for (Habit habit : user.habitlist) {
                        Log.d("Firebase", "Habit: " + habit.habitType + ", Emission: " + habit.emission);
                    }
                } else {
                    Log.d("Firebase", "User does not exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });

//        habitLogReminder();
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

        // Populate first spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(0);

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
                case "Plant-Base":
                    hint = "Number Consume:";
                    break;
                case "Buy New Clothes":
                case "Buy Electronics":
                case "Other Purchases":
                    hint = "Number Purchase";
                    break;
                case "Public Transportation":
                case "Flight":
                    hint = "Duration:";
                    detail = "Type:";
                    break;

                case "Energy Bills":
                    hint = "Amount";
                    break;
            }

            if (category == null || subcategory == null || userInput.isEmpty()) {
                Toast.makeText(this, "Please complete all fields before adding the activity.", Toast.LENGTH_SHORT).show();
                return;
            }

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
            activitiesMap.get(selectedDate).add(new Activity(selectedDate, activityDescription.toString(), subcategory));

            for (Habit habit : habitList){
                if (habit.getCategory().equal(subcategory)){
                    habit.addProgress();
                }
            }
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
//    private void habitLogReminder(){
//        List<Acitvity> activityList; //get activity from firebase
//        //may be original gethabit is list<string>, need to be updated
//        ArrayList<Habit> habitlist = getHabits2();
//        boolean if_logged = false;
//        for (Acitvity activity : activityList) {
//            for(Habit habit : habitlist){
//                if(activity.getCategory == habit.getCategroy){
//                    if_logged = true;
//                    break;
//                }
//            }
//        }
//        if (!if_logged){
//            new AlertDialog.Builder(this).setTitle("Habits are waiting to be logged!")
//                    .setPositiveButton("Got it", (dialog, which) -> {
//                        dialog.dismiss();
//                    })
//                    .show();
//        }
//    }
}


