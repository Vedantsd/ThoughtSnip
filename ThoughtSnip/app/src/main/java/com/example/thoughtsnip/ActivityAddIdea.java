package com.example.thoughtsnip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityAddIdea extends AppCompatActivity {

    EditText etTitle, etProblem, etSolution;
    Button btnSave;
    DBHelper dbHelper;

    // edit mode
    boolean isEdit = false;
    int editId = -1;
    Idea editingIdea = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idea);

        etTitle = findViewById(R.id.editTitle);
        etProblem = findViewById(R.id.editProblem);
        etSolution = findViewById(R.id.editSolution);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("editId")) {
            isEdit = true;
            editId = intent.getIntExtra("editId", -1);

            if (editId != -1) {
                editingIdea = dbHelper.getIdeaById(editId);
                if (editingIdea != null) {
                    etTitle.setText(editingIdea.getTitle());
                    etProblem.setText(editingIdea.getProblemStatement());
                    etSolution.setText(editingIdea.getSolution());
                    btnSave.setText("Update");
                } else {
                    isEdit = false;
                }
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = etTitle.getText().toString().trim();
                String problem = etProblem.getText().toString().trim();
                String solution = etSolution.getText().toString().trim();
                String dateTime = new java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        java.util.Locale.getDefault()
                ).format(new java.util.Date());

                if (title.isEmpty()) {
                    etTitle.setError("Title required");
                    return;
                }

                if (isEdit && editingIdea != null) {
                    editingIdea.setTitle(title);
                    editingIdea.setProblemStatement(problem);
                    editingIdea.setSolution(solution);
                    editingIdea.setDateTime(dateTime);

                    boolean ok = dbHelper.updateIdea(editingIdea);
                    if (ok) {
                        Toast.makeText(ActivityAddIdea.this, "Updated", Toast.LENGTH_SHORT).show();
                        finish(); // go back to MainActivity which will refresh in onResume()
                    } else {
                        Toast.makeText(ActivityAddIdea.this, "Update failed", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Idea newIdea = new Idea(-1, title, problem, solution, dateTime);
                    boolean ok = dbHelper.addIdea(newIdea);
                    if (ok) {
                        Toast.makeText(ActivityAddIdea.this, "Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityAddIdea.this, "Save failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
