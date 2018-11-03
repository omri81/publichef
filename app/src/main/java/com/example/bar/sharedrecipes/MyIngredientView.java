package com.example.bar.sharedrecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyIngredientView extends LinearLayout {
    protected EditText ingredientET;
    protected TextView ingredientCounterTV;
    protected View cancelBtn;
    protected View quantityTV;
    protected View dotTV;
    protected EditText quantityET;

    public MyIngredientView(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            inflater.inflate(R.layout.ingrediant_layout, this);
        }
        ingredientET = (EditText) findViewById(R.id.ingredient_layout_edit_text);
        ingredientCounterTV = (TextView) findViewById(R.id.ingredient_layout_counter);
        cancelBtn = findViewById(R.id.ingredient_layout_X);
        quantityET = (EditText) findViewById(R.id.ingredient_layout_quantity_ET);
    }

}
