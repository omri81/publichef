package com.example.bar.sharedrecipes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyRecipes extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SqliteRecyclerViewAdapter adapter;
    private Button delete;
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private SQLiteDatabase dbw;
    private ArrayList<String> urls;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        myRef = FirebaseDatabase.getInstance().getReference("Recipe");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://sharedrecipes-65510.appspot.com");
        recyclerView = (RecyclerView) findViewById(R.id.Activity_my_recipes_recycler_view);
        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();
        dbw = helper.getWritableDatabase();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SqliteRecyclerViewAdapter(getAllItems(),storageReference,urls,this,helper,dbw);
        recyclerView.setAdapter(adapter);
        delete = (Button) findViewById(R.id.Activity_my_recipes_deleteBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.execSQL("delete from "+ DatabaseHelper.TABLE_NAME);

            }
        });
    }
    private ArrayList<Recipe> getAllItems() {
        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME, null);
        ArrayList<Recipe> recipes = new ArrayList<>();
        urls = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                String chef = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_CHEF_NAME));
                String recipeName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RECIPE_NAME));
                String dishKind = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DISH_KIND));
                String ingredients = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_INGREDIENTS));
                String methods = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_METHODS));
                String url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_URI));
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID));
                urls.add(url);
                recipes.add(new Recipe(chef,recipeName,url,dishKind,methods,ingredients,id));
            }while(cursor.moveToNext());
        }

        return recipes;
    }
}
