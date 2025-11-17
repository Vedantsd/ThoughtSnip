package com.example.thoughtsnip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton fabAdd;
    DBHelper dbHelper;
    ArrayList<Idea> ideaList;
    ArrayList<String> ideaTitles;   // For ListView display
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        fabAdd = findViewById(R.id.fab_add);

        dbHelper = new DBHelper(this);

        loadIdeas();

        // Add new idea
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ActivityAddIdea.class);
                startActivity(i);
            }
        });

        // View selected idea
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Idea idea = ideaList.get(position);

                Intent intent = new Intent(MainActivity.this, ActivityViewIdea.class);
                intent.putExtra("id", idea.getId());
                intent.putExtra("title", idea.getTitle());
                intent.putExtra("problem", idea.getProblemStatement());
                intent.putExtra("solution", idea.getSolution());
                intent.putExtra("date", idea.getDateTime());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIdeas();
    }

    private void loadIdeas() {
        ideaList = dbHelper.getAllIdeas();
        IdeaAdapter ideaAdapter = new IdeaAdapter(this, ideaList);
        listView.setAdapter(ideaAdapter);
    }
}