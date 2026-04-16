package com.rorycd.quiz;

// Defines a question as a title, description, and set of potential answers
public class Question {
    String title;
    String description;
    Answer[] answers;

    Question(String title, String description, Answer[] answers) {
        this.title = title;
        this.description = description;
        this.answers = answers;
    }
}
