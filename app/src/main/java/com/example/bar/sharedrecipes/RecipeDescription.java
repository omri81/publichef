package com.example.bar.sharedrecipes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class RecipeDescription extends AppCompatActivity {
    private TextView chefsName, recipeName, ingredientsTV,methodsTV, dishKind;
    private Recipe recipe;
    private ImageView iv;
    private String downloadUrl;
    private Intent intent;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button addToMyRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("sharedRecipe");
        downloadUrl = intent.getStringExtra("downloadUrl");

        chefsName = (TextView) findViewById(R.id.Activity_Recipe_Description_name);
        recipeName = (TextView) findViewById(R.id.Activity_Recipe_Description_recipe_name);
        ingredientsTV = (TextView) findViewById(R.id.Activity_Recipe_Description_ingredients);
        methodsTV = (TextView) findViewById(R.id.Activity_Recipe_Description_Methods);
        dishKind = (TextView) findViewById(R.id.Activity_recipe_Description_recipe_dish_kind);
        iv = (ImageView) findViewById(R.id.Activity_Recipe_Description_Photo);

        chefsName.setText(recipe.getPersonName());
        recipeName.setText(recipe.getRecipeName());
        dishKind.setText(recipe.getDishKind());
        if (recipe.getIngredients() != null) {
            ingredientsTV.setText(ingredientsToString());
        }else{
            ingredientsTV.setText(recipe.getIngredientString());
        }
        if(recipe.getStepsMethod()!= null) {
            methodsTV.setText(methodsToString());
        }else{
            methodsTV.setText(recipe.getMethods());
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://sharedrecipes-65510.appspot.com");
        setImagefromFirebase();
        addToMyRecipes = (Button) findViewById(R.id.Activity_Recipe_Description_AddToMyRecipes);
        addToMyRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper helper = new DatabaseHelper(RecipeDescription.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COL_CHEF_NAME,recipe.getPersonName());
                values.put(DatabaseHelper.COL_RECIPE_NAME,recipe.getRecipeName());
                values.put(DatabaseHelper.COL_DISH_KIND,recipe.getDishKind());
                values.put(DatabaseHelper.COL_INGREDIENTS,ingredientsToString());
                values.put(DatabaseHelper.COL_METHODS,methodsToString());
                values.put(DatabaseHelper.COL_URI,downloadUrl);
                db.insert(DatabaseHelper.TABLE_NAME,null,values);
                Toast.makeText(RecipeDescription.this, "Recipe Added To My Recipes", Toast.LENGTH_SHORT).show();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDescription.this,BiggerPictureActivity.class);
                intent.putExtra("downloadPic",downloadUrl);
                intent.putExtra("recipe",recipe);
                startActivity(intent);
            }
        });
    }

    private String ingredientsToString() {
        String ingredientsString = "";
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            String ing = "*" + recipe.getIngredients().get(i).getName() + " - " +
                    recipe.getIngredients().get(i).getQuantity();
            ingredientsString += ing + "\n";
        }
        return ingredientsString;
    }

    private String methodsToString() {
        String methodsString = "";
        for (int i = 0; i < recipe.getStepsMethod().size(); i++) {
            String ing = "*" + recipe.getStepsMethod().get(i);
            methodsString += ing + "\n";
        }
        return methodsString;
    }

    private void setImagefromFirebase(){
        if(isOnline()) {
            storageReference.child(downloadUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(iv);
                }
            });
        }else{
            iv.setImageResource(R.drawable.icons8wifioff100);
        }
    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
