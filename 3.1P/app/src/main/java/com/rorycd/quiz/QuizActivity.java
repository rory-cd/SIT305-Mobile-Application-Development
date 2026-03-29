package com.rorycd.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuizActivity extends AppCompatActivity {

    // State
    enum State {
        PENDING,
        SELECTED,
        SUBMITTED
    }
    State state;
    QuestionManager questionMgr;
    int selectedIdx;
    int score;
    String name;

    // UI Elements
    TextView tvCounter;
    TextView tvTitle;
    TextView tvDescription;
    LinearLayout llAnswers;
    Button[] buttons;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        score = 0;

        questionMgr = new QuestionManager();
        tvCounter = findViewById(R.id.tvCounter);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> OnNext());
        llAnswers = findViewById(R.id.llAnswers);

        LoadNextQuestion();
    }

    protected void LoadNextQuestion() {
        // Reset
        state = State.PENDING;
        llAnswers.removeAllViews();

        // Counter
        String counterText = getString(R.string.counter_text, questionMgr.GetQuestionNumber(), questionMgr.QuestionCount());
        tvCounter.setText(counterText);

        // Set title and description
        Question question = questionMgr.GetQuestion();
        tvTitle.setText(question.title);
        tvDescription.setText(question.description);

        // Add a button for each answer
        Answer[] answers = question.answers;
        buttons = new Button[answers.length];
        for (int i = 0; i < answers.length; i++) {
            // Create button and set attributes
            Button btn = new Button(this);
            btn.setText(answers[i].text);
            btn.setBackgroundColor(getColor(R.color.potential_answer));
            final int idx = i;
            btn.setOnClickListener(v -> OnAnswerSelected(idx));
            // Add to layout
            llAnswers.addView(btn);
            // Add to buttons array for reference
            buttons[i] = btn;
        }
    }

    protected void OnAnswerSelected(int idx) {

        // If there hasn't been an answer submitted yet
        if (state != State.SUBMITTED) {

            selectedIdx = idx;

            // Style the buttons
            for (int i = 0; i < buttons.length; i++) {
                if (i == selectedIdx) {
                    ApplySelectButtonStyle(buttons[i]);
                }
                else {
                    ResetButtonStyle(buttons[i]);
                }
            }

            btnNext.setText(R.string.submit_button_text);
            state = State.SELECTED;
        }
    }

    protected void OnNext() {

        // If an answer has been selected, submit
        if (state == State.SELECTED) {

            if (questionMgr.IsCorrectAnswer(selectedIdx))
                score++;

            // Apply correct/incorrect colours
            for (int i = 0; i < buttons.length; i++) {
                if (questionMgr.IsCorrectAnswer(i)) {
                    ApplyCorrectButtonStyle(buttons[i]);
                }
                else if (i == selectedIdx && !questionMgr.IsCorrectAnswer(i)) {
                    ApplyIncorrectButtonStyle(buttons[i]);
                }
            }
            state = State.SUBMITTED;
            btnNext.setText(R.string.next_button_text);
        }
        // If the answer has already been submitted
        else if (state == State.SUBMITTED) {
            // Load the next question
            if (questionMgr.HasQuestionsRemaining()) {
                questionMgr.NextQuestion();
                LoadNextQuestion();
            // Show results
            } else {
                Intent intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("score", score);
                intent.putExtra("total", questionMgr.questions.length);
                startActivity(intent);
                finish();
            }
        }

    }

    protected void ApplySelectButtonStyle(Button btn) {
        btn.setBackgroundColor(getColor(R.color.selected_answer));
        btn.setTextColor(getColor(R.color.black));
    }

    protected void ApplyCorrectButtonStyle(Button btn) {
        btn.setBackgroundColor(getColor(R.color.correct_answer));
        btn.setTextColor(getColor(R.color.white));
    }

    protected void ApplyIncorrectButtonStyle(Button btn) {
        btn.setBackgroundColor(getColor(R.color.incorrect_answer));
        btn.setTextColor(getColor(R.color.white));
    }

    protected void ResetButtonStyle(Button btn) {
        btn.setBackgroundColor(getColor(R.color.potential_answer));
        btn.setTextColor(getColor(R.color.black));
    }
}