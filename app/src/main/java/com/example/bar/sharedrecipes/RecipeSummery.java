package com.example.bar.sharedrecipes;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class RecipeSummery extends AppCompatActivity implements View.OnClickListener {

    private TextView chefsName;
    private TextView recipeName;
    private TextView ingredientsTV;
    private TextView methodsTV;
    private Recipe recipe;
    private ImageView recipePhoto;
    private View doneBtn;
    private TextView dishKind;
    private Intent intent;
    private DatabaseReference myRef;
    private StorageReference storageRef;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_summery);

        intent = getIntent();
        recipe = (Recipe)intent.getSerializableExtra("recipe");
        myRef = FirebaseDatabase.getInstance().getReference("Recipe");
        String key = myRef.push().getKey();

        chefsName = (TextView) findViewById(R.id.Activity_Recipe_Summery_name);
        recipeName = (TextView) findViewById(R.id.Activity_Recipe_Summery_recipe_name);
        dishKind = (TextView) findViewById(R.id.Activity_recipe_Summery_recipe_dish_kind);
        ingredientsTV = (TextView) findViewById(R.id.Activity_Recipe_Summery_ingredients);
        methodsTV = (TextView) findViewById(R.id.Activity_Recipe_Summery_Methods);
        doneBtn = findViewById(R.id.Activity_Recipe_Summery_doneBtn);
        doneBtn.setOnClickListener(this);

        chefsName.setText(recipe.getPersonName());
        recipeName.setText(recipe.getRecipeName());
        dishKind.setText(recipe.getDishKind());

        ingredientsTV.setText(ingredientsToString());

        methodsTV.setText(methodsToString());
        recipePhoto = (ImageView) findViewById(R.id.Activity_Recipe_Summery_Photo);

        //Picasso.with(this).load(recipe.getUri()).into(recipePhoto);
        //Uri uri = getImageUri(this, recipe.getBitmap());
        //Picasso.get().load(uri).into(recipePhoto);
        Uri uri = Uri.parse(recipe.getUri());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Picasso.get().load(uri).into(recipePhoto);



    }


    private String ingredientsToString() {
        String ingredientsString = "";
        int counter = 1;
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            String ing = counter++ +"." + recipe.getIngredients().get(i).getName() + " - " +
                    recipe.getIngredients().get(i).getQuantity();
            ingredientsString += ing + "\n";
        }
        return ingredientsString;
    }

    private String methodsToString() {
        String methodsString = "";
        int counter = 1;
        for (int i = 0; i < recipe.getStepsMethod().size(); i++) {
            String ing = counter++ +"." + recipe.getStepsMethod().get(i);
            methodsString += ing + "\n";
        }
        return methodsString;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public static byte[] convertBitmapToByteArrayUncompressed(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
    public void uploadingImageToFB(String key){
        storageRef = FirebaseStorage.getInstance().getReference().child(key);
        byte[] bytes = convertBitmapToByteArrayUncompressed(bitmap);
        if(bytes == null){
            Log.d("bitmapt","bitmap is null");
        }
        UploadTask uploadTask = storageRef.putBytes(bytes);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RecipeSummery.this, "uploaded successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RecipeSummery.this, "did not upload", Toast.LENGTH_SHORT).show();
                Log.d("omri",e.getMessage());
            }
        });

    }

    @Override
    public void onClick(View view) {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        LikesDatabaseHelper likesHelper = new LikesDatabaseHelper(this);
        SQLiteDatabase likeDB = likesHelper.getWritableDatabase();
        String key = myRef.push().getKey();
        ContentValues likeValues = new ContentValues();
        likeValues.put(LikesDatabaseHelper.COL_ID,key);
        likeValues.put(LikesDatabaseHelper.COL_LIKE,0);
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CHEF_NAME,recipe.getPersonName());
        values.put(DatabaseHelper.COL_RECIPE_NAME,recipe.getRecipeName());
        values.put(DatabaseHelper.COL_DISH_KIND,recipe.getDishKind());
        values.put(DatabaseHelper.COL_INGREDIENTS,ingredientsToString());
        values.put(DatabaseHelper.COL_METHODS,methodsToString());
        values.put(DatabaseHelper.COL_URI,key);
        db.insert(DatabaseHelper.TABLE_NAME,null,values);
        myRef.child(key).setValue(recipe);
        uploadingImageToFB(key);
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
