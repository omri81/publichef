package com.example.bar.sharedrecipes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LikesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "likesDB";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "likes";
    public static final String COL_ID = "id";
    public static final String COL_LIKE = "liked";


    private static final String CREATE_COMMAND = "CREATE TABLE "+TABLE_NAME+" ("+
            COL_ID+" TEXT, "+
            COL_LIKE+" INTEGER"+
            ")";

    public LikesDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME );
        onCreate(db);
    }
}