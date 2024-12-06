package com.example.cscb07_project;

import static com.google.firebase.database.DatabaseKt.getDatabase;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HabitSuggestionActivity extends AppCompatActivity {

    private SearchView searchView;
    private ListView predefinedListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<Habit> predefinedSuggestionList;
    public ArrayList<Habit> adoptedHabitList;
    private HabitAdapter adoptedHabitAdapter;
    private ArrayList<String> suggestionList;
    private String userName;
    private String userId;
    private ArrayList<String> predefinedSuggestionNames;
    private ArrayList<String> filteredResult;
    private RecyclerView adoptedListView;

    private Spinner typeSpinner, ImpactSpinner;
    private EditText impactLowerBound;
    private EditText impactUpperBound;
    private FirebaseDatabase database;
    private DatabaseReference realtimeRef;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;
    private String todayDate;
    private ListView personalizedSuggestion;
    private ArrayAdapter<String> personalizedSuggestionAdapter;

    //*******private List<Habit> habitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habit_suggestion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        adoptedListView= findViewById(R.id.adoptedListView);
        database = FirebaseDatabase.getInstance();
        realtimeRef =  database.getReference("users");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        userId = currentUser.getUid();
        adoptedHabitList = new ArrayList<>();
        typeSpinner = findViewById(R.id.typeSpinner);
        initializeTypeSpinner();
        impactLowerBound = findViewById(R.id.ImpactLowerbound);
        impactUpperBound = findViewById(R.id.ImpactUpperbound);
        dateFormat =  new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());
        calendar = Calendar.getInstance();
        todayDate = dateFormat.format(calendar.getTime());


        //Pre-defined Habit List
        predefinedSuggestionList = new ArrayList<Habit>();
        predefinedSuggestionList.add(new Habit("Cycling or Walking",0, "Biking or Walking to work",0, 0));
        predefinedSuggestionList.add(new Habit("Public Transportation",0, "Taking the bus",0, 0));
        predefinedSuggestionList.add(new Habit("Flight",0, "Taking No flight",0, 0));
        predefinedSuggestionList.add(new Habit("Plant-Based",0, "Making more of diet vegetables ",0, 0));
        predefinedSuggestionList.add(new Habit("Buy New Clothes",0, "Buy no new clothes",0, 0));
        predefinedSuggestionList.add(new Habit("Buy Electronics",0, "Buy no eletronics",0, 0));
        predefinedSuggestionList.add(new Habit("Energy Bills",0, "Taking shorter showers",0, 0));
        predefinedSuggestionNames = new ArrayList<>();
        filteredResult = new ArrayList<>();
        predefinedSuggestionNames = getHabitStringList(predefinedSuggestionList);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replace with EcoTrackerMain
                Intent intent = new Intent(HabitSuggestionActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //LayoutGradiantAnimation
//        ConstraintLayout layout = findViewById(R.id.main);
//        ColorDrawable Layoutbackground = new ColorDrawable(Color.parseColor("green"));
//        layout.setBackground(Layoutbackground);
//
//        ObjectAnimator gradiant = ObjectAnimator.ofObject(Layoutbackground, "color", new ArgbEvaluator(),
//                Color.parseColor("#3F51B5"), Color.parseColor("#009999"));
//        gradiant.setDuration(3000);
//        gradiant.start();
//
//        //ButtonGradiantAnimation
//        ColorDrawable buttonbackground = new ColorDrawable(Color.parseColor("green"));
//        backButton.setBackground(buttonbackground);
//        ObjectAnimator Buttongradiant = ObjectAnimator.ofObject(buttonbackground, "color", new ArgbEvaluator(),
//                Color.parseColor("#3F51B5"), Color.parseColor("#009999"));
//        Buttongradiant.setDuration(3000);
//        Buttongradiant.start();

        //Search and list
        searchView = findViewById(R.id.searchView);
        predefinedListView= findViewById(R.id.listView);

        //binding array with listView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(getHabitStringList(predefinedSuggestionList)));
        predefinedListView.setAdapter(adapter);

        adoptedHabitAdapter = new HabitAdapter(adoptedHabitList);

        adoptedListView.setLayoutManager(new LinearLayoutManager(this));
        adoptedListView.setAdapter(adoptedHabitAdapter);

        predefinedListView.setOnItemClickListener((parent,view,position,ID) -> {
            Habit selectedHabit = predefinedSuggestionList.get(position);
            boolean if_habit_already_exists = false;
            for(Habit habit: adoptedHabitList) {
                if(habit.getAct().equals(selectedHabit.getAct())){
                    if_habit_already_exists = true;
                    Toast.makeText(HabitSuggestionActivity.this, "Already Adopted This Habit", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if(!if_habit_already_exists){
                ConfirmHabbit(selectedHabit);
            }
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
                //searchNew(change);
                return true;
            }
        });
        getUserHabits();
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters(searchView.getQuery().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        impactLowerBound.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                applyFilters(searchView.getQuery().toString());
            }
        });

        impactUpperBound.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                applyFilters(searchView.getQuery().toString());
            }
        });
        DatabaseReference habitListRef = realtimeRef.child(userId);
        habitListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        Map<String, Information> userInformation = user.getUserInformation();
                        Information info = userInformation.get(todayDate);
                        personalizedSuggestion = findViewById(R.id.personalizedSuggestion);
                        personalizedSuggestionAdapter = new ArrayAdapter<>(
                                HabitSuggestionActivity.this,
                                android.R.layout.simple_list_item_1,
                                new ArrayList<>(getHabitStringList(PersonalizedHabits(info)))
                        );
                        personalizedSuggestion.setAdapter(personalizedSuggestionAdapter);
//                    callback.onUserInfoRetrieved(info);
                    } else {
//                    callback.onUserInfoRetrieved(null); // Handle case where user is null
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
//                callback.onError(error);
               }
        });



        predefinedListView.setOnItemClickListener((parent, view, position, ID) -> {
            Habit selectedHabit = predefinedSuggestionList.get(position);
            boolean if_habit_already_exists = false;
            for (Habit habit : adoptedHabitList) {
                if (habit.getAct().equals(selectedHabit.getAct())) {
                    if_habit_already_exists = true;
                    Toast.makeText(HabitSuggestionActivity.this, "Already Adopted This Habit", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (!if_habit_already_exists) {
                ConfirmHabbit(selectedHabit);
            }
        });


    }

    private void initializeTypeSpinner() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{
                "All Categories", "Transportation", "Food", "Consumption","Energy"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

    }
    //still a fix list shows up
    private void search(String input) {
        filteredResult.clear();

        if (input.isEmpty()) {
            filteredResult.addAll(getHabitStringList(predefinedSuggestionList));
        } else {
            for (Habit habit : predefinedSuggestionList) {
                if (habit.getAct().toLowerCase().contains(input.toLowerCase())) {
                    filteredResult.add(habit.getAct());
                }
            }
        }
        adapter.clear();
        adapter.addAll(filteredResult);
        adapter.notifyDataSetChanged();
    }

    //PlaceHolder, merge with Log Activity/Calendar feature

    /*******Habit selectedHabit_trueHabit;*********/
    //String selectedHabit = selectedHabit_trueHabit.name;
    private void ConfirmHabbit(Habit selectedHabit){
        new AlertDialog.Builder(this).setTitle("Confirm Your Habit to Adopt")
                .setNegativeButton("Yes", (dialog, which) -> {
                    adoptedHabitList.add(selectedHabit);
                    adoptedHabitAdapter.notifyDataSetChanged();
                    realTimeAddHabit(adoptedHabitList);
                    Log.d("AdoptedHabits", adoptedHabitList.toString());
                    GotoLog();
                    //suggestionList.remove(selectedHabit);
                })
                .setPositiveButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void GotoLog(){
        new AlertDialog.Builder(this).setTitle("Let's get start!")
                .setMessage("Eco-changes start from log the activity in to your calender")
                .setNegativeButton("Go to Log it", (dialog, which) -> {
                    Intent intent = new Intent(HabitSuggestionActivity.this, CalendarActivity.class);
                    startActivity(intent);
                })
                .setPositiveButton("Log Later", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private  ArrayList<String> getHabitStringList(ArrayList<Habit> habitList){
        ArrayList<String> habitStringList = new ArrayList<>();
        for(Habit habit:habitList){
            habitStringList.add(habit.getAct());
        }
        return habitStringList;
    }

    private void getUserHabits(){
        DatabaseReference habitListRef = realtimeRef.child(userId).child("habitList");
        habitListRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adoptedHabitList.clear();
                for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                    String action = habitSnapshot.child("act").getValue(String.class);
                    String category = habitSnapshot.child("category").getValue(String.class);
                    Double progress = habitSnapshot.child("progress").getValue(Double.class);
                    Double impact = habitSnapshot.child("impact").getValue(Double.class);
                    Double frequency = habitSnapshot.child("frequency").getValue(Double.class);
                    if (impact == null) impact = 0.0;
                    if (progress == null) progress = 0.0;
                    if (frequency == null) frequency = 0.0;
                    adoptedHabitList.add(new Habit(category, progress,action,impact, frequency));

                }
                adoptedHabitAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching habits: " + error.getMessage());
            }

        });
    }
    private void realTimeAddHabit(ArrayList<Habit> habitList){
        realtimeRef.child("c0TTmxo4P5bJQ83snICf3m2k01u2").child("habitList").setValue(habitList);

    }

    public boolean if_add_progress(Habit habit, Activity activity){

        return false;
    }
    public ArrayList<Habit> filterbyImpact(ArrayList<Habit> list, double start, double end)
    {
        ArrayList<Habit> suggestions = new ArrayList<Habit>();

        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getImpact() >= start||list.get(i).getImpact() <= end)
            {
                suggestions.add(list.get(i));
            }
        }

        return suggestions;
    }
    public boolean inIndexbounds(String[] arr, String str, int start, int end)
    {
        for(int i = start; i <= end; i++)
        {
            if(str.equals(arr[i]))
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Habit> filterbyType(ArrayList<Habit> list, String type)
    {
        ArrayList<Habit> suggestions = new ArrayList<Habit>();

        String[] subcategories = {"Drive Personal Vehicle", "Public Transportation", "Cycling or Walking", "Flight", "Beef", "Pork", "Chicken", "Fish", "Plant-Based",
                "Buy New Clothes", "Buy Electronics", "Other Purchases", "Energy Bills"};

        for(int i = 0; i < list.size(); i++)
        {
            if(type.equals("Transportation"))
            {
                if(inIndexbounds(subcategories, list.get(i).getCategory(), 0, 3))
                {
                    suggestions.add(list.get(i));
                }
            }

            else if(type.equals("Food"))
            {
                if(inIndexbounds(subcategories, list.get(i).getCategory(), 4, 8))
                {
                    suggestions.add(list.get(i));
                }
            }

            else if(type.equals("Consumption"))
            {
                if(inIndexbounds(subcategories, list.get(i).getCategory(), 9, 11))
                {
                    suggestions.add(list.get(i));
                }
            }
            else if(type.equals("Energy")&& list.get(i).getCategory().equals("Energy Bills"))
            {
                suggestions.add(list.get(i));
            }
        }

        return suggestions;
    }

    public ArrayList<Habit> filterbyKeyword(ArrayList<Habit> list, String keyword)
    {
        String[] subcategories = {"Drive Personal Vehicle", "Public Transportation", "Cycling or Walking", "Flight", "Beef", "Pork", "Chicken", "Fish", "Plant-Based",
                "Buy New Clothes", "Buy Electronics", "Other Purchases", "Energy Bills"};

        ArrayList<Habit> suggestions = new ArrayList<Habit>();

        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getCategory().contains(keyword)||list.get(i).getAct().contains(keyword))
            {
                suggestions.add(list.get(i));
            }

            else if(keyword.equals("Transportation"))
            {
                if(inIndexbounds(subcategories, list.get(i).getCategory(), 0, 3))
                {
                    suggestions.add(list.get(i));
                }
            }

            else if(keyword.equals("Food"))
            {
                if(inIndexbounds(subcategories, list.get(i).getCategory(), 4, 8))
                {
                    suggestions.add(list.get(i));
                }
            }

            else if(keyword.equals("Consumption"))
            {
                if(inIndexbounds(subcategories, list.get(i).getCategory(), 9, 11))
                {
                    suggestions.add(list.get(i));
                }
            }
            else if(keyword.equals("Energy")&& list.get(i).getCategory().equals("Energy Bills"))
            {
                suggestions.add(list.get(i));
            }
        }

        return suggestions;
    }

    private void applyFilters(String keyword) {
        String selectedCategory = typeSpinner.getSelectedItem().toString();
        String lowerBoundText = impactLowerBound.getText().toString();
        String upperBoundText = impactUpperBound.getText().toString();
        ArrayList<Habit> filteredHabits = new ArrayList<Habit>();

        double lowerBound = TextUtils.isEmpty(lowerBoundText) ? Double.MIN_VALUE : Double.parseDouble(lowerBoundText);
        double upperBound = TextUtils.isEmpty(upperBoundText) ? Double.MAX_VALUE : Double.parseDouble(upperBoundText);
        filteredHabits = filterbyKeyword(predefinedSuggestionList, keyword);

        if (!selectedCategory.equals("All Categories")) {
            filteredHabits = filterbyType(filteredHabits, selectedCategory);
        }
        filteredHabits = filterbyImpact(filteredHabits, lowerBound, upperBound);

        adapter.clear();
        adapter.addAll(getHabitStringList(filteredHabits));
        adapter.notifyDataSetChanged();
    }
    public ArrayList<Habit> PersonalizedHabits(Information info) {
        double average = 14.249212;

        double factor = 0.1;
        if (info == null) {Log.d("info fetched failed", "nfo fetched failed");}
        if (info.getDailyEmission() > average)
        {
            factor = info.getDailyEmission();
        }

        ArrayList<Habit> suggestions = new ArrayList<Habit>();


        for(int i = 0; i < info.getActivityList().size(); i++)
        {
            String[] description = info.getActivityList().get(i).getDescription().split(":");
            double frequency = Double.parseDouble(description[description.length - 1].replaceAll("[^0-9\\\\.]", ""));
            //gets the numerical value of the actity from the description
            if(info.getActivityList().get(i).getSubCategory().equals("Drive Personal Vehicle"))
            {

                suggestions.add(new Habit("Drive Personal Vehicle", 0,  "Walk, bike or use more Public Transportation until Driving Distance: " + frequency*factor, info.getActivityList().get(i).getEmission()*factor,frequency*factor));
            }

            else if(info.getActivityList().get(i).getSubCategory().equals("Public Transportation"))
            {

                suggestions.add(new Habit("Public Transportation", 0,  "Plan routes more efficently until Public Transportation: " + frequency*factor, info.getActivityList().get(i).getEmission()*factor,frequency*factor));
            }

            else if(info.getActivityList().get(i).getSubCategory().equals("Flight"))
            {

                suggestions.add(new Habit("Flight", 0, "Fly less." + description[0] + ":" + description[0] + ":" + (int)(frequency*factor),info.getActivityList().get(i).getEmission()*factor,  (int)(frequency*factor)));
            }

            else if(info.getActivityList().get(i).getCategory().equals("Food")&&!(info.getActivityList().get(i).getSubCategory().equals("Plant-Based")))
            {

                suggestions.add(new Habit(info.getActivityList().get(i).getSubCategory(), 0,  "Eat less meat and more plant based food until "+info.getActivityList().get(i).getSubCategory()+": "+ frequency*factor,info.getActivityList().get(i).getEmission()*factor, frequency*factor));
            }

            else if(info.getActivityList().get(i).getCategory().equals("Consumption"))
            {

                suggestions.add(new Habit(info.getActivityList().get(i).getSubCategory(), 0,  info.getActivityList().get(i).getSubCategory()+" less until : "+ frequency*factor,info.getActivityList().get(i).getEmission()*factor, frequency*factor));
            }

            else if(info.getActivityList().get(i).getCategory().equals("Energy Bills"))
            {

                suggestions.add(new Habit(info.getActivityList().get(i).getSubCategory(), 0, "Take shorter showers, use less heating and AC, use natural light, use energy efficent products, until Energy Bill: " + frequency*factor, info.getActivityList().get(i).getEmission()*factor, frequency*factor));
            }
        }

        //goes through list and makes personalized habit suggestions based on the activity of the user, using the factor to calculate how to bring the emissions down to average
        return suggestions;
    }

//    public interface UserInfoCallback {
//        void onUserInfoRetrieved(Information info);
//        void onError(DatabaseError error);
//    }


}