package com.example.bar.sharedrecipes;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String name;
    private String quantity;

    public Ingredient(){}

    public Ingredient(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }
}
