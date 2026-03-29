package com.rorycd.quiz;

public class QuestionManager {

    int currentIdx = 0;

    Question[] questions = new Question[]{
        new Question(
                "Geography",
                "What is the capital of Iceland?",
                new Answer[]{
                        new Answer("Warnambool", false),
                        new Answer("Timbuktu", true),
                        new Answer("Florence", false)
                }),
                new Question(
                        "History",
                        "What is a battle?",
                        new Answer[]{
                                new Answer("What's a battle?", true),
                                new Answer("A stick", false),
                                new Answer("Pikachu", false)
                        })
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
