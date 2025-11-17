package com.example.thoughtsnip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ideas.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "ideas";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_PROBLEM = "problem";
    private static final String COLUMN_SOLUTION = "solution";
    private static final String COLUMN_DATETIME = "datetime";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_PROBLEM + " TEXT, " +
                COLUMN_SOLUTION + " TEXT, " +
                COLUMN_DATETIME + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addIdea(Idea idea) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, idea.getTitle());
        cv.put(COLUMN_PROBLEM, idea.getProblemStatement());
        cv.put(COLUMN_SOLUTION, idea.getSolution());
        cv.put(COLUMN_DATETIME, idea.getDateTime());

        long result = db.insert(TABLE_NAME, null, cv);
        return result != -1;
    }

    public ArrayList<Idea> getAllIdeas() {
        ArrayList<Idea> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String problem = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROBLEM));
                String solution = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOLUTION));
                String datetime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATETIME));

                list.add(new Idea(id, title, problem, solution, datetime));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
}