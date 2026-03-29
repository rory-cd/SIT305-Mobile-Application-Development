package com.rorycd.quiz;

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
