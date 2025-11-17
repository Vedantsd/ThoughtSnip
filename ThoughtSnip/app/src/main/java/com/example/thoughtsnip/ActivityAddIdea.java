package com.example.thoughtsnip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityAddIdea extends AppCompatActivity {

    EditText editTitle, editProblem, editSolution;
    Button btnSave;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idea);

        editTitle = findViewById(R.id.editTitle);
        editProblem = findViewById(R.id.editProblem);
        editSolution = findViewById(R.id.editSolution);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DBHelper(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveIdea();
            }
        });
    }

    private void saveIdea() {
        String title = editTitle.getText().toString().trim();
        String problem = editProblem.getText().toString().trim();
        String solution = editSolution.getText().toString().trim();

        if (title.isEmpty()) {
            editTitle.setError("Title required");
            return;
        }

        String dateTime = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(new Date());

        Idea idea = new Idea(title, problem, solution, dateTime);

        boolean inserted = dbHelper.addIdea(idea);

        if (inserted) {
            Toast.makeText(this, "Idea Saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving idea", Toast.LENGTH_SHORT).show();
        }
    }
}
