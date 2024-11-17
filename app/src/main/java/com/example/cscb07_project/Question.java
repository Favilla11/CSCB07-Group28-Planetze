package com.example.cscb07_project;

public class Question {
    private int questionId;
    private String questionText, category;
    private String[] option;

    public Question(int questionId, String questionText, String category, String[] option){
        this.questionId = questionId;
        this.questionText = questionText;
        this.category = category;
        this.option = option;
    }
    public String getQuestionText(){
        return questionText;
    }
    public int getQuestionId() {
        return questionId;
    }
    public String[] getOption(){
        return option;
    }
    public String getCategory(){
        return category;
    }
}
