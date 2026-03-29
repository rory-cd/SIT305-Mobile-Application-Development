package com.rorycd.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultsActivity extends AppCompatActivity {

    TextView tvCongrats;
    TextView tvScoreTitle;
    TextView tvScore;
    Button btnNewQuiz;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvCongrats = findViewById(R.id.tvCongrats);
        tvScoreTitle = findViewById(R.id.tvScoreTitle);
        tvScore = findViewById(R.id.tvScore);
        btnNewQuiz = findViewById(R.id.btnNewQuiz);
        btnFinish = findViewById(R.id.btnFinish);

        Intent intent = getIntent();

        // Set congrats text
        String name = intent.getStringExtra("name");
        String congratsText = getString(R.string.congrats_text, name);
        tvCongrats.setText(congratsText);

        // Set score text
        int score = intent.getIntExtra("score", 0);
        int total = intent.getIntExtra("total", 0);
        String scoreText = getString(R.string.score_text, score, total);
        tvScore.setText(scoreText);

        // Set button functionality
        btnNewQuiz.setOnClickListener(v -> startNewQuiz(name));
        btnFinish.setOnClickListener(v -> backToStart());
    }

    protected void startNewQuiz(String name) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    protected void backToStart() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}