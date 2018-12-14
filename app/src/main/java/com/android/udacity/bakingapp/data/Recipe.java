package com.android.udacity.bakingapp.data;

import java.util.ArrayList;
import java.util.List;


// Classe que representa todos os dados referentes a uma receita
public class Recipe {
    private long mId;
    private String mName;
    private int mServings;
    private String mImageUrl;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;

    public Recipe(long id, String name, int servings, String imageUrl) {
        this.mId = id;
        this.mName = name;
        this.mServings = servings;
        this.mImageUrl = imageUrl;
        this.mIngredients = new ArrayList<>();
        this.mSteps = new ArrayList<>();
    }

    public void addIngredient(double quantity, String measure, String name) {
        mIngredients.add(new Ingredient(quantity, measure, name));
    }

    public void addStep(long id, String shortDescription, String description, String videoUrl,
                        String thumbnailUrl) {
        mSteps.add(new Step(id, shortDescription, description, videoUrl, thumbnailUrl));
    }

    public long getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public int getServings() {
        return this.mServings;
    }

    public String getImageUrl() {
        return this.mImageUrl;
    }

    public List<Ingredient> getIngredients() {
        return this.mIngredients;
    }

    public List<Step> getSteps() {
        return this.mSteps;
    }
}

