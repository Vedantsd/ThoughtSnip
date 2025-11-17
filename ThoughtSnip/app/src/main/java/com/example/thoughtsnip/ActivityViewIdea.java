package com.example.thoughtsnip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ActivityViewIdea extends AppCompatActivity {

    TextView textTitle, textDate, textProblem, textSolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_idea);

        textTitle = findViewById(R.id.textTitle);
        textDate = findViewById(R.id.textDate);
        textProblem = findViewById(R.id.textProblem);
        textSolution = findViewById(R.id.textSolution);

        String title = getIntent().getStringExtra("title");
        String problem = getIntent().getStringExtra("problem");
        String solution = getIntent().getStringExtra("solution");
        String date = getIntent().getStringExtra("date");

        // Set data
        textTitle.setText(title);
        textDate.setText("Saved On: " + date);
        textProblem.setText(problem);
        textSolution.setText(solution);
    }
}
