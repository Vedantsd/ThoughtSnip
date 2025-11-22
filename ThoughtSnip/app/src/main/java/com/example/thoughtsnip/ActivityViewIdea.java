package com.example.thoughtsnip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityViewIdea extends AppCompatActivity {

    TextView textTitle, textDate, textProblem, textSolution;
    DBHelper dbHelper;
    FloatingActionButton fabAsk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_idea);

        textTitle = findViewById(R.id.textTitle);
        textDate = findViewById(R.id.textDate);
        textProblem = findViewById(R.id.textProblem);
        textSolution = findViewById(R.id.textSolution);
        fabAsk = findViewById(R.id.fab_ask);

        dbHelper = new DBHelper(this);

        int id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(this, "Error loading idea", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Idea idea = dbHelper.getIdeaById(id);
        if (idea == null) {
            Toast.makeText(this, "Idea not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textTitle.setText(idea.getTitle());
        textDate.setText("Saved On: " + idea.getDateTime());
        textProblem.setText(idea.getProblemStatement());
        textSolution.setText(idea.getSolution());

        fabAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityViewIdea.this, ActivityAskGemini.class);
                i.putExtra("title", idea.getTitle());
                i.putExtra("problem", idea.getProblemStatement());
                i.putExtra("solution", idea.getSolution());
                startActivity(i);
            }
        });
    }
}
