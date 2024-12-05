package com.example.cscb07_project;
import java.util.ArrayList;

public class QuestionBank {
    public static ArrayList<Question> getQuestions() {
        ArrayList<Question> questions = new ArrayList<>();

        String[] options1 = {"Yes", "No"};
        questions.add(new Question(1, "Do you own or regularly use a car?","Transportation", options1));

        String[] options2 = {"Gasoline", "Diesel", "Hybrid", "Electric", "I don’t know"};
        questions.add(new Question(2, "What type of car do you drive?","Transportation", options2));

        String[] options3 = {"<= 5,000 km", "5,000–10,000 km", "10,000–15,000 km", "15,000–20,000 km", "20,000–25,000 km", ">=25,000 km"};
        questions.add(new Question(3, "How many kilometers/miles do you drive per year?","Transportation", options3));

        String[] options4 = {"Never", "Occasionally (1-2 times/week)","Frequently (3-4 times/week)","Always (5+ times/week)"};
        questions.add(new Question(4, "How often do you use public transportation (bus, train,subway)?","Transportation", options4));

        String[] options5 = {"Under 1 hour", "1-3 hours","3-5 hours","5-10 hours","More than 10 hours"};
        questions.add(new Question(5, "How much time do you spend on public transport per week?","Transportation", options5));

        String[] options6 = {"None", "1-2 flights","3-5 flights","6-10 flights","More than 10 flights"};
        questions.add(new Question(6, "How many short-haul flights (less than 1,500 km / 932 miles) have you taken in the past year?","Transportation", options6));

        String[] options7 = {"None", "1-2 flights","3-5 flights","6-10 flights","More than 10 flights"};
        questions.add(new Question(7, "How many long-haul flights (more than 1,500 km / 932 miles) have you taken in the past year?","Transportation", options7));

        String[] options8 = {"Vegetarian", "Vegan","Pescatarian (fish/seafood)","Meat-based (eat all types of animal products)"};
        questions.add(new Question(8, "What best describes your diet?","Food", options8));

        String[] options9 = {"Beef", "Pork","Chicken","Fish/Seafood"};
        questions.add(new Question(9, "How often do you eat the following animal-based products?","Food", options9));

        String[] options10 = {"Never", "Rarely","Occasionally","Frequently"};
        questions.add(new Question(10, "How often do you waste food or throw away uneaten leftovers?","Food", options10));

        String[] options11 = {"Detached house", "Semi-detached house","Townhouse","Condo/Apartment", "Other"};
        questions.add(new Question(11, "What type of home do you live in?","Housing", options11));

        String[] options12 = {"1", "2","3-4","5 or more"};
        questions.add(new Question(12, "How many people live in your household?","Housing", options12));

        String[] options13 = {"Under 1000 sq. ft.", "1000-2000 sq. ft.","Over 2000 sq. ft."};
        questions.add(new Question(13, "What is the size of your home?","Housing", options13));

        String[] options14 = {"Natural Gas", "Electricity","Oil","Propane","Wood","Other"};
        questions.add(new Question(14, "What type of energy do you use to heat your home?","Housing", options14));

        String[] options15 = {"Under $50", "$50-$100","$100-$150","$150-$200", "Over $200"};
        questions.add(new Question(15, "What is your average monthly electricity bill?","Housing", options15));

        String[] options16 = {"Natural Gas", "Electricity","Oil","Propane", "Solar", "Other"};
        questions.add(new Question(16, "What type of energy do you use to heat water in your home?","Housing", options16));

        String[] options17 = {"Yes, primarily (more than 50% of energy use)", "Yes, partially (less than 50% of energy use)","No"};
        questions.add(new Question(17, "Do you use any renewable energy sources for electricity or heating?","Housing", options17));

        String[] options18 = {"Monthly", "Quarterly","Annually","Rarely"};
        questions.add(new Question(18, "How often do you buy new clothes?","Consumption", options18));

        String[] options19 = {"Yes, regularly", "Yes, occasionally","No"};
        questions.add(new Question(19, "Do you buy second-hand or eco-friendly products?","Consumption", options19));

        String[] options20 = {"None", "1","2","3 or more"};
        questions.add(new Question(20, "How many electronic devices have you purchased in the past year?","Consumption", options20));

        String[] options21 = {"Never", "Occasionally", "Frequently", "Always"};
        questions.add(new Question(21, "How often do you recycle?","Consumption", options21));

        return questions;
    }
}
