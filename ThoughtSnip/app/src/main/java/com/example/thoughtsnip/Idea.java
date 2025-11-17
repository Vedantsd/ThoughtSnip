package com.example.thoughtsnip;

public class Idea {

    private int id;
    private String title;
    private String problemStatement;
    private String solution;
    private String dateTime;

    public Idea(int id, String title, String problemStatement, String solution, String dateTime) {
        this.id = id;
        this.title = title;
        this.problemStatement = problemStatement;
        this.solution = solution;
        this.dateTime = dateTime;
    }

    public Idea(String title, String problemStatement, String solution, String dateTime) {
        this.title = title;
        this.problemStatement = problemStatement;
        this.solution = solution;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getProblemStatement() {
        return problemStatement;
    }

    public String getSolution() {
        return solution;
    }

    public String getDateTime() {
        return dateTime;
    }
}
