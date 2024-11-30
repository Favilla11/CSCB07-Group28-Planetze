package com.example.cscb07_project;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabbitSuggestion extends AppCompatActivity {

    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> suggestionList;
    public ArrayList<String> adoptedHabitList;
    private ArrayAdapter<String> adoptedHabitAdapter;

    //*******private List<Habit> habitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habbit_suggestion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //habitLogReminder:

        //***Firebase:
        //*******habitList = new ArrayList<Habit>();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replace with EcoTrackerMain
                Intent intent = new Intent(HabbitSuggestion.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //LayoutGradiantAnimation
        ConstraintLayout layout = findViewById(R.id.main);
        ColorDrawable Layoutbackground = new ColorDrawable(Color.parseColor("green"));
        layout.setBackground(Layoutbackground);

        ObjectAnimator gradiant = ObjectAnimator.ofObject(Layoutbackground, "color", new ArgbEvaluator(),
                Color.parseColor("#3F51B5"), Color.parseColor("#009999"));
        gradiant.setDuration(3000);
        gradiant.start();

        //ButtonGradiantAnimation
        ColorDrawable buttonbackground = new ColorDrawable(Color.parseColor("green"));
        backButton.setBackground(buttonbackground);
        ObjectAnimator Buttongradiant = ObjectAnimator.ofObject(buttonbackground, "color", new ArgbEvaluator(),
                Color.parseColor("#3F51B5"), Color.parseColor("#009999"));
        Buttongradiant.setDuration(3000);
        Buttongradiant.start();

        //Search and list
        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.listView);

        //List Placeholder
        suggestionList = new ArrayList<>();
        suggestionList.add("Drink Water");
        suggestionList.add("Walking, not Driving");
        suggestionList.add("Reading");

        //binding array with listView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(suggestionList));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent,view,position,ID) -> {
            String selectedHabit = adapter.getItem(position);
            ConfirmHabbit(selectedHabit);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String input) {
                //search(input);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String change) {
                search(change);
                return true;
            }
        });

        //habit tracking progress display
        adoptedHabitList = getDisplayList();
        //for (Map.Entry<String, Integer> habit : habitsMap){
            //adoptedHabitList.put(habit.getKey(),habit.getValue());
        //}

        ListView adoptedListView;
        adoptedListView= findViewById(R.id.adoptedListView);
        adoptedHabitAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, new ArrayList<>(adoptedHabitList));
        adoptedListView.setAdapter(adoptedHabitAdapter);



    }

    private void search(String input) {
        ArrayList<String> filteredResult = new ArrayList<>();
        if (input.isEmpty()){
            adapter.clear();
            filteredResult.addAll(suggestionList);
            adapter.notifyDataSetChanged();
        }
        for (String habit : suggestionList) {
                if (habit.toLowerCase().contains(input.toLowerCase())) {
                    filteredResult.add(habit);
                }
            }
        adapter.clear();
        adapter.addAll(filteredResult);
        adapter.notifyDataSetChanged();
    }

    //PlaceHolder, merge with Log Activity/Calendar feature


    /*******Habit selectedHabit_trueHabit;*********/
    //String selectedHabit = selectedHabit_trueHabit.name;
    private void ConfirmHabbit(String selectedHabit){
        new AlertDialog.Builder(this).setTitle("Confirm Your Habit to Adopt")
                .setNegativeButton("Yes", (dialog, which) -> {

                    ///*******addHabit(Habit)*****;
                    ///*******directly update habit into habitList in firebase/*******;
                    GotoLog(selectedHabit);
                    suggestionList.remove(selectedHabit);
                })
                .setPositiveButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void GotoLog(String selectedHabit){
        new AlertDialog.Builder(this).setTitle("Let's get start!")
                .setMessage("Eco-changes start from log the activity in to your calender")
                .setNegativeButton("Go to Log it", (dialog, which) -> {
                    //addToAdoptedHabit() to firebase;
                    //adoptedHabitList.put(selectedHabit, getProgress(selectedHabit));
                    adoptedHabitAdapter.clear();
                    adoptedHabitAdapter.addAll(adoptedHabitList);
                    adoptedHabitAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(HabbitSuggestion.this, LogInActivity.class);
                    intent.putExtra("selectedHabit", selectedHabit);
                    startActivity(intent);
                })
                .setPositiveButton("Log Later", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }


    //Placeholders
    private List<Map.Entry<String, Integer>> getHabits1(){
        HashMap<String, Integer> dummy = new HashMap<>();
        dummy.put("Drinking water", 10);

        int length = dummy.size();
        for (int i = 0; i< length; i++){
            dummy.get(i);
        }

        return new ArrayList<>();
    }
    private ArrayList<String> getHabits2(){
        //***get habit list data form firebase
        List<String> dummy = new ArrayList<>();
        dummy.add("Drinking water");

        return new ArrayList<>(dummy);
    }

    //getProgress PlaceHolder
    private int getProgress(String selectedHabit){
        return 3;
    }

    private ArrayList<String> getDisplayList() {
        ArrayList<String> habitList= getHabits2();
        int length = habitList.size();
        ArrayList<String> display = new ArrayList<>();
        for (String habit : habitList){
            display.add("You have already kept " + habit + " for " + getProgress(habit) + " Day(s) !");
        }
        return display;
    }

    /********Remind to update*****/
    //private addHabit(Habit habit){
        //habitList.add(habit);//update to firebase
    // }
}