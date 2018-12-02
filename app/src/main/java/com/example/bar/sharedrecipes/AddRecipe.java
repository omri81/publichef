package com.example.bar.sharedrecipes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class AddRecipe extends AppCompatActivity implements View.OnClickListener {
    private Spinner dishKindSpinner;
    private LinearLayout ingredientLL;
    private LinearLayout methodsLL;
    private Button doneBtn;
    private String s = "";

    private int ingredientCounter = 1;
    private MyIngredientView firstIngredient;
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private ArrayList<MyIngredientView> ingredients = new ArrayList<>();

    private int methodsCounter = 1;
    private MyMethodsStepsView firstMethod;
    private ArrayList<String> methodsList = new ArrayList<>();
    private ArrayList<MyMethodsStepsView> methods = new ArrayList<>();

    private ImageView recipeImg;
    private static Bitmap bitmap;


    private EditText chefsName;
    private EditText recipeName;

    private Recipe finishedRecipe;
    private static Uri recipeUri;
    private static Boolean photoChanged;
    private Boolean done = false;
    private String dishKind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        dishKindSpinner = (Spinner) findViewById(R.id.Activity_AddRecipe_dish_spinner);
        spinnerAdapter();
        ingredientLL = (LinearLayout) findViewById(R.id.Activity_AddRecipe_Ingrdients_layout);
        doneBtn = (Button) findViewById(R.id.Activity_AddRecipe_done_button);

        firstIngredient = new MyIngredientView(this);
        ingredientLL.addView(firstIngredient);
        ingredients.add(firstIngredient);
        firstIngredient.cancelBtn.setOnClickListener(this);
        firstIngredient.cancelBtn.setTag(ingredientCounter);
        firstIngredient.ingredientET.setHint("ingredient");

        methodsLL = (LinearLayout) findViewById(R.id.Activity_Add_Recipe_Methods_Layout);
        firstMethod = new MyMethodsStepsView(this);
        methods.add(firstMethod);
        methodsLL.addView(firstMethod);
        firstMethod.methodsCancelBtn.setOnClickListener(this);
        firstMethod.methodsCancelBtn.setTag(methodsCounter);
        firstMethod.methodsET.setHint("next step to follow");

        recipeImg = (ImageView) findViewById(R.id.Activity_Add_Recipe_Photo);


        chefsName = (EditText) findViewById(R.id.Activity_AddRecipe_Chef_Name);
        recipeName = (EditText) findViewById(R.id.Activity_AddRecipe_Recipe_Name);
        photoChanged = false;







    }


    private void spinnerAdapter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dishes_kinds_array,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dishKindSpinner.setAdapter(adapter);
    }

    public void createNewIngredient(View view) {

        MyIngredientView myView = new MyIngredientView(this);
        ingredientLL.addView(myView);
        ingredients.add(myView);

        myView.ingredientET.setHint("ingredient");
        myView.ingredientCounterTV.setText("" + ++ingredientCounter);
        myView.cancelBtn.setOnClickListener(this);
        myView.cancelBtn.setTag(ingredientCounter);

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ingredient_layout_X:
                int index = Integer.parseInt((v.getTag().toString()))-1;
                ingredientLL.removeView(ingredients.get(index));
                ingredients.remove(ingredients.get(index));
                ingredientCounter--;
                if(ingredients.size()>0){
                    for (int i = 0; i < ingredients.size() ; i++) {
                        ingredients.get(i).ingredientCounterTV.setText("" + (i+1));
                        ingredients.get(i).cancelBtn.setTag(i+1);
                    }
                }
                break;
            case R.id.method_steps_X:
                int methodIndex = Integer.parseInt((v.getTag().toString()))-1;
                methodsLL.removeView(methods.get(methodIndex));
                methods.remove(methods.get(methodIndex));
                methodsCounter--;
                if (methods.size() > 0) {
                    for (int i = 0; i < methods.size()  ; i++) {
                        methods.get(i).methodsCounterTV.setText("" + (i+1));
                        methods.get(i).methodsCancelBtn.setTag("" + (i+1));
                    }
                }
                break;
        }


    }

    public void createNewMethod(View view) {

        MyMethodsStepsView newMethod = new MyMethodsStepsView(this);
        methodsLL.addView(newMethod);
        methods.add(newMethod);
        newMethod.methodsET.setHint("next step to follow");
        newMethod.methodsCounterTV.setText(""+ ++methodsCounter);
        newMethod.methodsCancelBtn.setOnClickListener(this);
        newMethod.methodsCancelBtn.setTag(methodsCounter);
    }

    public void openCameraFragment(View view) {
        done = false;
        MyCameraFragment dialog = new MyCameraFragment();
        dialog.show(getFragmentManager(),null);
    }
    public boolean validateInfo(){

        if(chefsName.getText().toString().isEmpty()){
            chefsName.setError("Fill in your name");
        }
        if(recipeName.getText().toString().isEmpty()){
            recipeName.setError("Fill in recipe name");
        }

        if (ingredients.size() <= 0){
            Toast.makeText(this, "Must add ingredients", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).ingredientET.getText().toString().isEmpty()){
                ingredients.get(i).ingredientET.setError("Fill in ingredient please");
                return false;
            }
            if(ingredients.get(i).quantityET.getText().toString().isEmpty()){
                ingredients.get(i).quantityET.setError("Fill in quantity please");
                return false;
            }

        }
        if(methods.size() <= 0){
            Toast.makeText(this, "Must add Steps to follow", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i <methods.size() ; i++) {
            if(methods.get(i).methodsET.getText().toString().isEmpty()){
                methods.get(i).methodsET.setError("Fill in method please");
                return false;
            }
        }
        if (photoChanged == false){
            Toast.makeText(this, "Add Photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(bitmap == null){

            return false;

        }
        if(dishKindSpinner.getSelectedItem().equals("Recipe type")){
            Toast.makeText(this, "must choose the recipe type", Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }

    public void finishRecipe(View view) {
        done = true;


        if(validateInfo()) {

            SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);


            for (int i = 0; i <ingredients.size() ; i++) {
                String ingredient = ingredients.get(i).ingredientET.getText().toString().trim();
                String quantity = ingredients.get(i).quantityET.getText().toString().trim();
                ingredientList.add(new Ingredient(ingredient,quantity));
            }

            for (int i = 0; i <methods.size() ; i++) {
                methodsList.add(methods.get(i).methodsET.getText().toString().trim());
            }

            //get the chef's name
            String name = chefsName.getText().toString().trim();

            //get the recipe name
            String recipe = recipeName.getText().toString().trim();
            String uri = "";
            if(prefs.contains("URI")){
                uri = prefs.getString("URI","null");
            }
           // Uri recipeUri = Uri.parse(uri);

            dishKind = dishKindSpinner.getSelectedItem().toString();

            finishedRecipe = new Recipe(name,recipe,methodsList,ingredientList,uri,dishKind);

            Intent intent = new Intent(this,RecipeSummery.class);
            intent.putExtra("recipe",finishedRecipe);
            startActivity(intent);



        }else{
            Toast.makeText(this, "Missing some details please review your recipe", Toast.LENGTH_SHORT).show();
        }
    }

    public Recipe getFinishedRecipe() {
        return finishedRecipe;

    }

    public static Uri getRecipeUri() {
        return recipeUri;
    }

    public static Boolean getPhotoChanged() {
        return photoChanged;
    }

    public static void setPhotoChanged(Boolean photoChanged) {
        AddRecipe.photoChanged = photoChanged;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (done == true) {
            ingredientList.clear();
            methodsList.clear();
        }
        /*if(RecipeSummery.isFinished == true){
            finish();
        }*/
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap mbitmap) {
        bitmap = mbitmap;
    }

    public static byte[] bitmatpToByte(){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }
}
