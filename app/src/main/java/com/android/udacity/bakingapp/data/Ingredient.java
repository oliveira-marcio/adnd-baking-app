package com.android.udacity.bakingapp.data;


import java.util.Locale;

// Classe que representa todos os dados referentes a um ingrediente de uma receita
public class Ingredient {
    private double mQuantity;
    private String mMeasure;
    private String mName;

    public Ingredient(double quantity, String measure, String name) {
        this.mQuantity = quantity;
        this.mMeasure = measure;
        this.mName = name;
    }

    public double getQuantity() {
        return this.mQuantity;
    }

    public String getMeasure() {
        return this.mMeasure;
    }

    public String getName() {
        return this.mName;
    }

    public String getFullIngredient() {
        final String BULLET = "\u2022";
        if (mQuantity == (long) mQuantity) {
            return String.format(Locale.getDefault(), "%s (%d %s) %s\n",
                    BULLET,
                    (long) mQuantity,
                    mMeasure,
                    mName);
        } else {
            return String.format(Locale.getDefault(), "%s (%.2f %s) %s\n",
                    BULLET,
                    mQuantity,
                    mMeasure,
                    mName);
        }
    }
}