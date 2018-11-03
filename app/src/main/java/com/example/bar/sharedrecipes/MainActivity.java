package com.example.bar.sharedrecipes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button deleteBtn;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private DatabaseReference myRef;
    private StorageReference storageReference;
    private FirebaseRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipes;
    private ArrayList<String> urls;
    private FirebaseStorage storage;
    private LikesDatabaseHelper likeHelper;
    private SQLiteDatabase likeDB;
    private SQLiteDatabase likeDBw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isOnline() == false) {
            Intent intent = new Intent(this, NoInternet.class);
            startActivity(intent);
            finish();
        }
        if (isOnline() == true) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    MyLoadingFragment fragment = new MyLoadingFragment();
                    fragment.show(getFragmentManager(), null);
                }
            });
            t.start();
        }


        myRef = FirebaseDatabase.getInstance().getReference("Recipe");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://sharedrecipes-65510.appspot.com");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.Activity_Main_Plus_Button);
        fab.setOnClickListener(this);
        ActivityCompat.requestPermissions(this, permissions, 1);
        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();
        recyclerView = (RecyclerView) findViewById(R.id.Activity_Main_Recycler_View);
        likeHelper = new LikesDatabaseHelper(this);
        likeDB = helper.getReadableDatabase();
        likeDBw = helper.getWritableDatabase();
        getAllRecipes();

    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, AddRecipe.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MyRecipes.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void clearDb(View view) {
        db.execSQL("delete from " + DatabaseHelper.TABLE_NAME);
    }

    public void getAllRecipes() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipes = new ArrayList<Recipe>();
                urls = new ArrayList<String>();
                //  Toast.makeText(MainActivity.this, recipes.size() + "", Toast.LENGTH_SHORT).show();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Recipe recipe = dataSnapshot1.getValue(Recipe.class);
                    urls.add(dataSnapshot1.getKey());
                    recipes.add(recipe);

                    LinearLayoutManager manger = new LinearLayoutManager(MainActivity.this);

                    manger.setStackFromEnd(true);
                    manger.setReverseLayout(true);
                    recyclerView.setLayoutManager(manger);
                    adapter = new FirebaseRecyclerViewAdapter(recipes, storageReference, urls, MainActivity.this, myRef, likeDB, likeDBw);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(MainActivity.this, "everything", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        myRef.addListenerForSingleValueEvent(valueEventListener);


    }

    public boolean addRecipe(Recipe r) {
        if (recipes.add(r)) {
            return true;
        }
        return false;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Integer> isliked() {

        ArrayList<Integer> likes = new ArrayList<>();
        Cursor cursor = likeDB.query(LikesDatabaseHelper.TABLE_NAME,
                new String[]{"liked"},
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                int likecounter = cursor.getColumnIndex(LikesDatabaseHelper.COL_LIKE);
                likes.add(likecounter);
            } while (cursor.moveToNext());
        }
        return likes;
    }
}

