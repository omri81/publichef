package com.example.bar.sharedrecipes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "recipesDB";
    private static final int DB_VERSION = 6;
    public static final String TABLE_NAME = "recipes2";
    public static final String COL_ID = "id";
    public static final String COL_RECIPE_NAME = "recipesName";
    public static final String COL_CHEF_NAME = "chefsName";
    public static final String COL_INGREDIENTS = "ingredients";
    public static final String COL_METHODS = "methods";
    public static final String COL_URI = "uri";
    public static final String COL_DISH_KIND = "dishKind";
    public static final String COL_IMAGE = "image";


    private static final String CREATE_COMMAND = "CREATE TABLE "+TABLE_NAME+" ("+
            COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COL_RECIPE_NAME+" TEXT, "+
            COL_CHEF_NAME+" TEXT, "+
            COL_INGREDIENTS+" TEXT, "+
            COL_METHODS+ " TEXT, "+
            COL_DISH_KIND+ " TEXT, "+
            COL_URI+" TEXT, "+
            COL_IMAGE+" BLOB"+
            ")";

    public DatabaseHelper(Context context) {
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