package com.rorycd.quiz;

// Holds a possible answer to a question along with a boolean indicating correctness
public class Answer {
    String text;
    Boolean isCorrect;

    Answer(String text, Boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }
}
