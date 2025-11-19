package com.example.thoughtsnip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    DBHelper dbHelper;
    ArrayList<Idea> ideaList;
    IdeaRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fab_add);

        dbHelper = new DBHelper(this);

        fabAdd.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ActivityAddIdea.class);
            startActivity(i);
        });

        loadIdeas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIdeas();
    }

    private void loadIdeas() {

        ideaList = dbHelper.getAllIdeas();

        adapter = new IdeaRecyclerAdapter(
                this,
                ideaList,

                // CLICK → open idea
                idea -> {
                    Intent i = new Intent(MainActivity.this, ActivityViewIdea.class);
                    i.putExtra("id", idea.getId());
                    startActivity(i);
                }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        enableSwipe();
    }

    private void enableSwipe() {

        SwipeGesture swipeGesture = new SwipeGesture(this) {

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int pos = viewHolder.getAdapterPosition();
                Idea idea = ideaList.get(pos);

                if (direction == ItemTouchHelper.RIGHT) {

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete Idea?")
                            .setMessage("Are you sure you want to delete this idea?")
                            .setPositiveButton("Delete", (dialog, which) -> {

                                Idea deletedIdea = ideaList.get(pos);
                                dbHelper.deleteIdea(deletedIdea.getId());

                                adapter.removeItem(pos);

                                Snackbar.make(recyclerView, "Idea deleted", Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", v -> {
                                            dbHelper.addIdea(deletedIdea);
                                            loadIdeas();
                                        }).show();

                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                adapter.notifyItemChanged(pos);   // restore item
                            })
                            .show();
                }

                else if (direction == ItemTouchHelper.LEFT) {

                    Intent i = new Intent(MainActivity.this, ActivityAddIdea.class);
                    i.putExtra("editId", idea.getId());
                    startActivity(i);

                    adapter.notifyItemChanged(pos);
                }
            }
        };

        new ItemTouchHelper(swipeGesture).attachToRecyclerView(recyclerView);
    }
}