package com.example.cscb07_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
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

        addActivityButton.setOnClickListener(v -> showAddActivityDialog());
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


