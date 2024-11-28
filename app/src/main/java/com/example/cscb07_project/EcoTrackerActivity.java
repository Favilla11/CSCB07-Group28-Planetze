package com.example.cscb07_project;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EcoTrackerActivity extends AppCompatActivity {

    // UI components
    private EditText editDistanceDriven, editTimePublicTransport, editDistanceCycledWalked, editFlights, editNumberOfServingsConsumed, editNumberOfClothingItemsPurchased, editNumberOfDevicesPurchased, editNumberOtherPurchases, editEnergyBill;
    private Spinner spinnerTransportationType, spinnerFoodConsumptionType, spinnerConsumptionAndShoppingType, spinnerPersonalVehicleType, spinnerPublicVehicleType, spinnerFlightType, spinnerActivityType, spinnerEnergyBillType;
    private Button btnCalculateEmissions, btnClearAll;
    private TextView txtTotalEmissions;
    private TextView txtPartEmissions;

    private double totalEmissions = 0.0;

    private double emissionsTransportationActivities = 0.0;
    private double emissionsFoodConsumptionActivities = 0.0;
    private double emissionsConsumptionShoppingActivities = 0.0;

    private double emissionsDriving = 0.0;
    private double emissionsPublicTransport = 0.0;
    private double emissionsCyclingWalking = 0.0;
    private double emissionsFlights = 0.0;
    private double emissionsBeef = 0.0;
    private double emissionsPork = 0.0;
    private double emissionsChicken = 0.0;
    private double emissionsFish = 0.0;
    private double emissionsPlant = 0.0;
    private double emissionsBuyNewClothes = 0.0;
    private double emissionsBuyElectronics = 0.0;
    private double emissionsOtherPurchases = 0.0;
    private double emissionsEnergyBills = 0.0;
    private double emissionsElectricityBills = 0.0;
    private double emissionsGasBills = 0.0;
    private double emissionsWaterBills = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecotracker);

        // Initialize views
        editDistanceDriven = findViewById(R.id.edit_distance_driven);
        editTimePublicTransport = findViewById(R.id.edit_time_public_transport);
        editNumberOfServingsConsumed = findViewById(R.id.edit_number_of_servings_consumed);
        editDistanceCycledWalked = findViewById(R.id.edit_distance_cycled_walked);
        editFlights = findViewById(R.id.edit_flights);
        editNumberOfClothingItemsPurchased = findViewById(R.id.edit_number_of_clothing_items_purchased);
        editNumberOfDevicesPurchased = findViewById(R.id.edit_number_of_devices_purchased);
        editNumberOtherPurchases = findViewById(R.id.edit_number_of_the_purchases);
        editEnergyBill = findViewById(R.id.edit_energy_bill);
        spinnerTransportationType = findViewById(R.id.spinner_transportation_type);
        spinnerFoodConsumptionType = findViewById(R.id.spinner_food_consumption_type);
        spinnerConsumptionAndShoppingType = findViewById(R.id.spinner_consumption_and_shopping_type);
        spinnerPersonalVehicleType = findViewById(R.id.spinner_personal_vehicle_type); // For selecting vehicle type
        spinnerPublicVehicleType = findViewById(R.id.spinner_public_vehicle_type); // For selecting vehicle type
        spinnerFlightType = findViewById(R.id.spinner_flight_type); // For selecting flight type
        spinnerActivityType = findViewById(R.id.spinner_activity_type); // New Spinner for Transport or Food
        spinnerEnergyBillType = findViewById(R.id.spinner_energy_bill_type);
        btnCalculateEmissions = findViewById(R.id.btn_calculate_emissions);
        btnClearAll = findViewById(R.id.btn_clear_all);
        txtTotalEmissions = findViewById(R.id.txt_total_emissions);
        txtPartEmissions = findViewById(R.id.txt_part_emissions);

        spinnerTransportationType.setVisibility(View.VISIBLE);
        spinnerFoodConsumptionType.setVisibility(View.GONE);
        spinnerConsumptionAndShoppingType.setVisibility(View.GONE);
        spinnerPersonalVehicleType.setVisibility(View.GONE);
        spinnerPublicVehicleType.setVisibility(View.GONE);
        spinnerFlightType.setVisibility(View.GONE);
        spinnerActivityType.setVisibility(View.VISIBLE);
        editDistanceDriven.setVisibility(View.GONE);
        editTimePublicTransport.setVisibility(View.GONE);
        editDistanceCycledWalked.setVisibility(View.GONE);
        editFlights.setVisibility(View.GONE);
        editNumberOfServingsConsumed.setVisibility(View.GONE);
        editNumberOfClothingItemsPurchased.setVisibility(View.GONE);
        editNumberOfDevicesPurchased.setVisibility(View.GONE);
        editNumberOtherPurchases.setVisibility(View.GONE);
        editEnergyBill.setVisibility(View.GONE);

        // Calculate emissions when button is clicked
        btnCalculateEmissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateEmissions();
            }
        });

        // Clear all data when button is clicked
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllData();
            }
        });

        // Listen to the activity type spinner to determine if Transport or Food consumption is selected
        spinnerActivityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                handleActivityTypeChange(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // Listen to the transportation type spinner for showing/hiding related fields
        spinnerTransportationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                handleTransportationTypeChange(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // Listen to the transportation type spinner for showing/hiding related fields
        spinnerFoodConsumptionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                handleMealTypeChange(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // Listen to the transportation type spinner for showing/hiding related fields
        spinnerConsumptionAndShoppingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                handleConsumptionShoppingTypeChange(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void handleActivityTypeChange(String selectedType) {
        // Show or hide the UI components based on selected activity type (Transport or Food Consumption)
        spinnerTransportationType.setVisibility(View.GONE);
        spinnerFoodConsumptionType.setVisibility(View.GONE);
        spinnerConsumptionAndShoppingType.setVisibility(View.GONE);
        spinnerPersonalVehicleType.setVisibility(View.GONE);
        spinnerPublicVehicleType.setVisibility(View.GONE);
        spinnerEnergyBillType.setVisibility(View.GONE);
        spinnerFlightType.setVisibility(View.GONE);
        spinnerActivityType.setVisibility(View.VISIBLE);
        editDistanceDriven.setVisibility(View.GONE);
        editTimePublicTransport.setVisibility(View.GONE);
        editDistanceCycledWalked.setVisibility(View.GONE);
        editFlights.setVisibility(View.GONE);
        editNumberOfServingsConsumed.setVisibility(View.GONE);
        editNumberOfClothingItemsPurchased.setVisibility(View.GONE);
        editNumberOfDevicesPurchased.setVisibility(View.GONE);
        editNumberOtherPurchases.setVisibility(View.GONE);
        editEnergyBill.setVisibility(View.GONE);

        if (selectedType.equals("Transport")) {
            // Show transportation-related fields
            spinnerTransportationType.setVisibility(View.VISIBLE);
            editDistanceDriven.setVisibility(View.VISIBLE);  // Default field for driving
            editTimePublicTransport.setVisibility(View.VISIBLE); // Default field for public transport
            // Add other relevant fields related to transport
        } else if (selectedType.equals("Food Consumption")) {
            // Show fields related to food consumption (for example, food type or quantity)
            // Hide transport-related fields
            spinnerFoodConsumptionType.setVisibility(View.VISIBLE);
            editNumberOfServingsConsumed.setVisibility(View.VISIBLE);
        } else if (selectedType.equals("Consumption and Shopping")) {
            // Show fields related to food consumption (for example, food type or quantity)
            // Hide transport-related fields
            spinnerConsumptionAndShoppingType.setVisibility(View.VISIBLE);
        }
    }

    private void handleTransportationTypeChange(String selectedType) {
        // Show or hide UI components based on selected transportation type
        spinnerTransportationType.setVisibility(View.VISIBLE);
        spinnerFoodConsumptionType.setVisibility(View.GONE);
        spinnerConsumptionAndShoppingType.setVisibility(View.GONE);
        spinnerPersonalVehicleType.setVisibility(View.GONE);
        spinnerPublicVehicleType.setVisibility(View.GONE);
        spinnerEnergyBillType.setVisibility(View.GONE);
        spinnerFlightType.setVisibility(View.GONE);
        spinnerActivityType.setVisibility(View.VISIBLE);
        editDistanceDriven.setVisibility(View.GONE);
        editTimePublicTransport.setVisibility(View.GONE);
        editDistanceCycledWalked.setVisibility(View.GONE);
        editFlights.setVisibility(View.GONE);
        editNumberOfServingsConsumed.setVisibility(View.GONE);
        editNumberOfClothingItemsPurchased.setVisibility(View.GONE);
        editNumberOfDevicesPurchased.setVisibility(View.GONE);
        editNumberOtherPurchases.setVisibility(View.GONE);
        editEnergyBill.setVisibility(View.GONE);

        if (selectedType.equals("drive personal vehicle")) {
            spinnerPersonalVehicleType.setVisibility(View.VISIBLE);
            editDistanceDriven.setVisibility(View.VISIBLE);
        } else if (selectedType.equals("public transport")) {
            spinnerPublicVehicleType.setVisibility(View.VISIBLE);
            editTimePublicTransport.setVisibility(View.VISIBLE);
        } else if (selectedType.equals("cycling or walking")) {
            editDistanceCycledWalked.setVisibility(View.VISIBLE);
        } else if (selectedType.equals("flights")) {
            spinnerFlightType.setVisibility(View.VISIBLE);
            editFlights.setVisibility(View.VISIBLE);
        }
    }
    private void handleMealTypeChange(String selectedType) {
        // Show or hide UI components based on selected transportation type
        spinnerPersonalVehicleType.setVisibility(View.GONE);
        spinnerPublicVehicleType.setVisibility(View.GONE);
        spinnerFlightType.setVisibility(View.GONE);
        spinnerFoodConsumptionType.setVisibility(View.VISIBLE);
        spinnerEnergyBillType.setVisibility(View.GONE);
        editDistanceDriven.setVisibility(View.GONE);
        editTimePublicTransport.setVisibility(View.GONE);
        editDistanceCycledWalked.setVisibility(View.GONE);
        editFlights.setVisibility(View.GONE);
        editNumberOfServingsConsumed.setVisibility(View.VISIBLE);
        editNumberOfClothingItemsPurchased.setVisibility(View.GONE);
        editNumberOfDevicesPurchased.setVisibility(View.GONE);
        editNumberOtherPurchases.setVisibility(View.GONE);
        editEnergyBill.setVisibility(View.GONE);
    }

    private void handleConsumptionShoppingTypeChange(String selectedType) {
        // Show or hide UI components based on selected transportation type
        if (selectedType.equals("Buy New Clothes")) {
            editDistanceDriven.setVisibility(View.GONE);
            spinnerPersonalVehicleType.setVisibility(View.GONE);
            editTimePublicTransport.setVisibility(View.GONE);
            spinnerPublicVehicleType.setVisibility(View.GONE);
            spinnerEnergyBillType.setVisibility(View.GONE);
            editDistanceCycledWalked.setVisibility(View.GONE);
            editFlights.setVisibility(View.GONE);
            spinnerFlightType.setVisibility(View.GONE);
            editNumberOfClothingItemsPurchased.setVisibility(View.VISIBLE);
            editNumberOfDevicesPurchased.setVisibility(View.GONE);
            editNumberOtherPurchases.setVisibility(View.GONE);
            editEnergyBill.setVisibility(View.GONE);
        } else if (selectedType.equals("Buy Electronics")) {
            editDistanceDriven.setVisibility(View.GONE);
            spinnerPersonalVehicleType.setVisibility(View.GONE);
            editTimePublicTransport.setVisibility(View.GONE);
            spinnerPublicVehicleType.setVisibility(View.GONE);
            spinnerEnergyBillType.setVisibility(View.GONE);
            editDistanceCycledWalked.setVisibility(View.GONE);
            editFlights.setVisibility(View.GONE);
            spinnerFlightType.setVisibility(View.GONE);
            editNumberOfClothingItemsPurchased.setVisibility(View.GONE);
            editNumberOfDevicesPurchased.setVisibility(View.VISIBLE);
            editNumberOtherPurchases.setVisibility(View.GONE);
            editEnergyBill.setVisibility(View.GONE);
        } else if (selectedType.equals("Other Purchases")) {
            editDistanceDriven.setVisibility(View.GONE);
            spinnerPersonalVehicleType.setVisibility(View.GONE);
            editTimePublicTransport.setVisibility(View.GONE);
            spinnerPublicVehicleType.setVisibility(View.GONE);
            spinnerEnergyBillType.setVisibility(View.GONE);
            editDistanceCycledWalked.setVisibility(View.GONE);
            editFlights.setVisibility(View.GONE);
            spinnerFlightType.setVisibility(View.GONE);
            editNumberOfClothingItemsPurchased.setVisibility(View.GONE);
            editNumberOfDevicesPurchased.setVisibility(View.GONE);
            editNumberOtherPurchases.setVisibility(View.VISIBLE);
            editEnergyBill.setVisibility(View.GONE);
        } else if (selectedType.equals("Energy Bills")) {
            editDistanceDriven.setVisibility(View.GONE);
            spinnerPersonalVehicleType.setVisibility(View.GONE);
            editTimePublicTransport.setVisibility(View.GONE);
            spinnerPublicVehicleType.setVisibility(View.GONE);
            editDistanceCycledWalked.setVisibility(View.GONE);
            editFlights.setVisibility(View.GONE);
            spinnerFlightType.setVisibility(View.GONE);
            editNumberOfClothingItemsPurchased.setVisibility(View.GONE);
            editNumberOfDevicesPurchased.setVisibility(View.GONE);
            editNumberOtherPurchases.setVisibility(View.GONE);
            editEnergyBill.setVisibility(View.VISIBLE);
            spinnerEnergyBillType.setVisibility(View.VISIBLE);
        }
    }
    private void calculateEmissions() {
        try {
            String activityType = spinnerActivityType.getSelectedItem().toString().toLowerCase();
            double emissionsForActivity = 0;

            if (activityType.equals("transport")) {
                String transportType = spinnerTransportationType.getSelectedItem().toString().toLowerCase();
                switch (transportType) {
                    case "drive personal vehicle":
                        emissionsDriving = logDriving();
                        break;
                    case "public transport":
                        emissionsPublicTransport = logPublicTransport();
                        break;
                    case "cycling or walking":
                        emissionsCyclingWalking = logCyclingWalking();
                        break;
                    case "flights":
                        emissionsFlights = logFlights();
                        break;
                    default:
                        Toast.makeText(this, "Unknown transport type", Toast.LENGTH_SHORT).show();
                        return;
                }
            } else if (activityType.equals("food consumption")) {
                String FoodConsumptionType = spinnerFoodConsumptionType.getSelectedItem().toString().toLowerCase();
                switch (FoodConsumptionType) {
                    case "beef":
                        emissionsBeef = logBeef();
                        break;
                    case "pork":
                        emissionsPork = logPork();
                        break;
                    case "chicken":
                        emissionsChicken = logChicken();
                        break;
                    case "fish":
                        emissionsFish = logFish();
                        break;
                    case "plant-based":
                        emissionsPlant = logPlant();
                        break;
                    default:
                        Toast.makeText(this, "Unknown transport type", Toast.LENGTH_SHORT).show();
                        return;
                }
            } else if (activityType.equals("consumption and shopping")) {
                String ConsumptionAndShoppingType = spinnerConsumptionAndShoppingType.getSelectedItem().toString();
                switch (ConsumptionAndShoppingType) {
                    case "Buy New Clothes":
                        emissionsBuyNewClothes = logBuyNewClothes();
                        break;
                    case "Buy Electronics":
                        emissionsBuyElectronics = logBuyElectronics();
                        break;
                    case "Other Purchases":
                        emissionsOtherPurchases = logOtherPurchases();
                        break;
                    case "Energy Bills":
                        emissionsEnergyBills = logEnergyBills();
                        break;
                    default:
                        Toast.makeText(this, "Unknown transport type", Toast.LENGTH_SHORT).show();
                        return;
                }
            }

            // Update total emissions
            totalEmissions = emissionsDriving + emissionsPublicTransport + emissionsCyclingWalking + emissionsFlights
                           + emissionsBeef + emissionsPork + emissionsChicken + emissionsFish + emissionsPlant
                           + emissionsBuyNewClothes + emissionsBuyElectronics + emissionsOtherPurchases + emissionsEnergyBills;
            emissionsTransportationActivities = emissionsDriving + emissionsPublicTransport + emissionsCyclingWalking + emissionsFlights;
            emissionsFoodConsumptionActivities = emissionsBeef + emissionsPork + emissionsChicken + emissionsFish + emissionsPlant;
            emissionsConsumptionShoppingActivities = emissionsBuyNewClothes + emissionsBuyElectronics + emissionsOtherPurchases + emissionsEnergyBills;
            txtTotalEmissions.setText("Total CO2 Emissions:               " + totalEmissions + " kg");
            txtPartEmissions.setText(String.format(
                            "Driving CO2 Emissions:             %.2f kg\n" +
                            "PublicTransport CO2 Emissions:     %.2f kg\n" +
                            "CyclingWalking CO2 Emissions:      %.2f kg\n" +
                            "Flights CO2 Emissions:             %.2f kg\n" +
                            "Beef Consume CO2 Emissions:        %.2f kg\n" +
                            "Pork Consume CO2 Emissions:        %.2f kg\n" +
                            "Chicken Consume CO2 Emissions:     %.2f kg\n" +
                            "Fish Consume CO2 Emissions:        %.2f kg\n" +
                            "Plant-based Consume CO2 Emissions: %.2f kg\n" +
                            "Buy New Clothes CO2 Emissions:     %.2f kg\n" +
                            "Buy Electronics CO2 Emissions:     %.2f kg\n" +
                            "Other Purchases CO2 Emissions:     %.2f kg\n" +
                            "Energy Bills CO2 Emissions:        %.2f kg\n",
                    emissionsDriving, emissionsPublicTransport, emissionsCyclingWalking,
                    emissionsFlights, emissionsBeef, emissionsPork, emissionsChicken,
                    emissionsFish, emissionsPlant, emissionsBuyNewClothes,
                    emissionsBuyElectronics, emissionsOtherPurchases, emissionsEnergyBills
            ));


        } catch (Exception e) {
            Toast.makeText(this, "Error calculating emissions", Toast.LENGTH_SHORT).show();
        }
    }

    private double logDriving() {
        String distanceStr = editDistanceDriven.getText().toString();
        if (distanceStr.isEmpty()) {
            Toast.makeText(this, "Please enter distance driven", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double distance = Double.parseDouble(distanceStr);

        // Adjust emissions based on vehicle type
        String vehicleType = spinnerPersonalVehicleType.getSelectedItem().toString().toLowerCase();
        double emissionsPerKm = 0.24; // Default for regular car
        if (vehicleType.equals("electric car")) {
            emissionsPerKm = 0.05; // Lower emissions for electric cars
        } else if (vehicleType.equals("suv")) {
            emissionsPerKm = 0.30; // Higher emissions for SUVs
        }

        return distance * emissionsPerKm; // CO2 per km for driving
    }

    private double logPublicTransport() {
        String vehicleType = spinnerPublicVehicleType.getSelectedItem().toString().toLowerCase();
        double emissionsPerKm = 0.24; // Default for regular car
        if (vehicleType.equals("bus")) {
            emissionsPerKm = 0.05; // Lower emissions for electric cars
        } else if (vehicleType.equals("train")) {
            emissionsPerKm = 0.30; // Higher emissions for SUVs
        } else if (vehicleType.equals("subway")) {
            emissionsPerKm = 0.30; // Higher emissions for SUVs
        }

        String timeStr = editTimePublicTransport.getText().toString();
        if (timeStr.isEmpty()) {
            Toast.makeText(this, "Please enter time spent on public transport", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double timeSpent = Double.parseDouble(timeStr);
        return timeSpent * 0.15; // CO2 per hour for public transport
    }

    private double logCyclingWalking() {
        // No emissions for cycling or walking
        return 0;
    }

    private double logFlights() {
        String flightsStr = editFlights.getText().toString();
        if (flightsStr.isEmpty()) {
            Toast.makeText(this, "Please enter number of flights", Toast.LENGTH_SHORT).show();
            return 0;
        }
        int numberOfFlights = Integer.parseInt(flightsStr);

        // Determine if the flights are short-haul or long-haul
        String flightType = spinnerFlightType.getSelectedItem().toString().toLowerCase();
        double emissionsPerFlight = 0.5; // Default for short-haul
        if (flightType.equals("long-haul")) {
            emissionsPerFlight = 2.0; // Higher emissions for long-haul flights
        }

        return numberOfFlights * emissionsPerFlight; // Example: 1.0 kg CO2 per flight for short-haul, 2.0 for long-haul
    }

    private double logBeef() {
        String number = editNumberOfServingsConsumed.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 60; // CO2 per hour for public transport
    }

    private double logPork() {
        String number = editNumberOfServingsConsumed.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 30; // CO2 per hour for public transport
    }

    private double logChicken() {
        String number = editNumberOfServingsConsumed.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 20; // CO2 per hour for public transport
    }

    private double logFish() {
        String number = editNumberOfServingsConsumed.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 10; // CO2 per hour for public transport
    }

    private double logPlant() {
        String number = editNumberOfServingsConsumed.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter number of serving consumed", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 2.5; // CO2 per hour for public transport
    }

    private double logBuyNewClothes() {
        String number = editNumberOfClothingItemsPurchased.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter number of clothing iterms purchased", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 10; // CO2 per hour for public transport
    }

    private double logBuyElectronics() {
        String number = editNumberOfDevicesPurchased.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter number of devices purchased", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 100; // CO2 per hour for public transport
    }

    private double logOtherPurchases() {
        String number = editNumberOtherPurchases.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter number of other purchases", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 50; // CO2 per hour for public transport
    }

    private double logEnergyBills() {
        String BillType = spinnerEnergyBillType.getSelectedItem().toString();
        if (BillType.equals("Electricity")){
            emissionsElectricityBills = logElectricityBills();
        } else if (BillType.equals("Gas")){
            emissionsWaterBills = logWaterBills();
        } else if (BillType.equals("Water")){
            emissionsGasBills = logGasBills();
        }

        return emissionsElectricityBills + emissionsWaterBills + emissionsGasBills;
    }

    private double logElectricityBills() {
        String number = editEnergyBill.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter electricity bill", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 0.5; // CO2 per hour for public transport
    }

    private double logWaterBills() {
        String number = editEnergyBill.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter water bill", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 0.5; // CO2 per hour for public transport
    }

    private double logGasBills() {
        String number = editEnergyBill.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter gas bill", Toast.LENGTH_SHORT).show();
            return 0;
        }
        double numberconsumed = Double.parseDouble(number);
        return numberconsumed * 0.5; // CO2 per hour for public transport
    }
    private void clearAllData() {
        editDistanceDriven.setText("");
        editTimePublicTransport.setText("");
        editDistanceCycledWalked.setText("");
        editFlights.setText("");
        editNumberOfServingsConsumed.setText("");
        editNumberOfClothingItemsPurchased.setText("");
        editNumberOfDevicesPurchased.setText("");
        editNumberOfServingsConsumed.setText("");
        editEnergyBill.setText("");
        totalEmissions = 0;
        txtTotalEmissions.setText("Total CO2 Emissions: 0 kg");
        txtPartEmissions.setText("");

        // Reset spinners
        spinnerActivityType.setSelection(0);
        spinnerTransportationType.setSelection(0);
        spinnerFoodConsumptionType.setSelection(0);
        spinnerConsumptionAndShoppingType.setSelection(0);
        spinnerPersonalVehicleType.setSelection(0);
        spinnerPublicVehicleType.setSelection(0);
        spinnerFlightType.setSelection(0);
        spinnerEnergyBillType.setSelection(0);
    }
}