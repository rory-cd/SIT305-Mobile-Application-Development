package com.rorycd.quiz;

// Provides a readable interface for question management
public class QuestionManager {

    private int currentIdx = 0;

    Question[] questions = new Question[]{
        new Question(
            "Geography",
            "Which ocean is the largest?",
            new Answer[]{
                new Answer("Atlantic", false),
                new Answer("Indian", false),
                new Answer("Arctic", false),
                new Answer("Pacific", true)
            }
        ),
        new Question(
            "Food",
            "What’s the main ingredient in guacamole?",
            new Answer[]{
                new Answer("Cucumber", false),
                new Answer("Tomato", false),
                new Answer("Courgette", false),
                new Answer("Avocado", true),
                new Answer("Egg", false),
                new Answer("Pickle", false),
                new Answer("Cheese", false),
                new Answer("Macaroni", false),
                new Answer("Milk", false),
            }
        ),
        new Question(
            "Mathematics",
            "What’s the smallest prime number?",
            new Answer[]{
                new Answer("1", false),
                new Answer("2", true),
                new Answer("3", false)
            }
        ),
        new Question(
            "Mythology",
            "A centaur is based on which creature?",
            new Answer[]{
                new Answer("Donkey", false),
                new Answer("Horse", true),
                new Answer("Human", true)
            }
        ),
        new Question(
            "Cosmology",
            "Which planet has the most moons?",
            new Answer[]{
                new Answer("Jupiter", false),
                new Answer("Saturn", true),
                new Answer("Neptune", false)
            }
        )
    };

    protected Question GetQuestion() {
        return questions[currentIdx];
    }

    protected int GetQuestionNumber() {
        return currentIdx + 1;
    }

    protected int QuestionCount() {
        return questions.length;
    }

    protected void NextQuestion() {
        currentIdx++;
    }

    protected boolean HasQuestionsRemaining() {
        return currentIdx < questions.length - 1;
    }

    protected boolean IsCorrectAnswer(int idx) {
        return questions[currentIdx].answers[idx].isCorrect;
    }
}
