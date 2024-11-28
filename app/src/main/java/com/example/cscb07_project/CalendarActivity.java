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
    private Button addEventButton;
    private RecyclerView eventsRecyclerView;

    private Map<String, List<Event>> eventsMap;
    private List<Event> currentDateEvents;
    private EventAdapter eventAdapter;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        addEventButton = findViewById(R.id.addEventButton);
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);

        eventsMap = new HashMap<>();
        currentDateEvents = new ArrayList<>();
        eventAdapter = new EventAdapter(currentDateEvents, new EventAdapter.OnEventClickListener() {
            @Override
            public void onEditClick(Event event, int position) {
                showEditEventDialog(event, position);
            }

            @Override
            public void onDeleteClick(Event event, int position) {
                deleteEvent(event, position);
            }
        });

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setAdapter(eventAdapter);

        selectedDate = getFormattedDate(calendarView.getDate());
        updateRecyclerView();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            updateRecyclerView();
        });

        addEventButton.setOnClickListener(v -> showAddEventDialog());
    }

    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Event");

        final EditText input = new EditText(this);
        input.setHint("Event Description");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String description = input.getText().toString().trim();
            if (!description.isEmpty()) {
                if (!eventsMap.containsKey(selectedDate)) {
                    eventsMap.put(selectedDate, new ArrayList<>());
                }
                eventsMap.get(selectedDate).add(new Event(selectedDate, description));
                updateRecyclerView();
                Toast.makeText(this, "Event Added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Event description cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void showEditEventDialog(Event event, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Event");

        final EditText input = new EditText(this);
        input.setText(event.getDescription());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newDescription = input.getText().toString().trim();
            if (!newDescription.isEmpty()) {
                event.setDescription(newDescription);
                eventAdapter.notifyItemChanged(position);
                Toast.makeText(this, "Event Updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateRecyclerView() {
        currentDateEvents.clear();
        if (eventsMap.containsKey(selectedDate)) {
            currentDateEvents.addAll(eventsMap.get(selectedDate));
        }
        eventAdapter.notifyDataSetChanged();
    }
    private void deleteEvent(Event event, int position) {
        currentDateEvents.remove(position);
        if (eventsMap.containsKey(selectedDate)) {
            eventsMap.get(selectedDate).remove(event);

            if (eventsMap.get(selectedDate).isEmpty()) {
                eventsMap.remove(selectedDate);
            }
        }
        eventAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Event Deleted!", Toast.LENGTH_SHORT).show();
    }

    private String getFormattedDate(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(timeInMillis));
    }
}


