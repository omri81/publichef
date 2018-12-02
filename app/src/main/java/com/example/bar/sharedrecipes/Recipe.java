package com.example.bar.sharedrecipes;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private String personName;
    private String recipeName;
    private ArrayList<String> stepsMethod;
    private ArrayList<Ingredient> ingredients;
    private String uri;
    private String dishKind;
    private String methods;
    private String ingredientString;
    private int id;
    private boolean liked;

    public Recipe(){}

    public Recipe(String personName, String recipeName, String uri, String dishKind, String methods, String ingredientString, int id) {
        this.personName = personName;
        this.recipeName = recipeName;
        this.uri = uri;
        this.dishKind = dishKind;
        this.methods = methods;
        this.ingredientString = ingredientString;
        this.id = id;

    }

    public Recipe(String personName, String recipeName, ArrayList<String> stepsMethod, ArrayList<Ingredient> ingredients, String uri, String dishKind) {
        this.personName = personName;
        this.recipeName = recipeName;
        //Recipe.bitmap = bitmap;
        this.stepsMethod = stepsMethod;
        this.ingredients = ingredients;
        this.uri = uri;
        this.dishKind = dishKind;



    }

    public String getPersonName() {
        return personName;
    }

    public String getRecipeName() {
        return recipeName;
    }


    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<String> getStepsMethod() {
        return stepsMethod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

 /*   public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }*/

    public String getDishKind() {
        return dishKind;
    }

    public String getIngredientString() {
        return ingredientString;
    }

    public String getMethods() {
        return methods;
    }

    public int getId() {
        return id;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    public boolean getLiked(){
        return liked;
    }

}

