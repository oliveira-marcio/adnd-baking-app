package com.android.udacity.bakingapp;

import com.android.udacity.bakingapp.data.Recipe;
import com.android.udacity.bakingapp.utilities.JsonUtils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class RecipeTest {

    // Teste de serialização e de-serialização da classe Recipe utilizando a library Gson
    @Test
    public void SerializeRecipeWithGson() throws Exception {

        Recipe recipeOriginal = new Recipe(1, "Recipe Name", 2, "image.jpg");
        recipeOriginal.addIngredient(10, "g", "Sugar");
        recipeOriginal.addIngredient(3.5, "l", "Water");
        recipeOriginal.addIngredient(50, "g", "Chocolate");
        recipeOriginal.addStep(1, "Step 1", "Detailed Step 1", "http://video/video1.mp4", null);
        recipeOriginal.addStep(2, "Step 2", "Detailed Step 2", null, "step2.jpg");

        String recipeJson = JsonUtils.RecipeToJson(recipeOriginal);
        Recipe recipeParsed = JsonUtils.JsonToRecipe(recipeJson);

        assertEquals(recipeOriginal.getId(), recipeParsed.getId());
        assertEquals(recipeOriginal.getName(), recipeParsed.getName());
        assertEquals(recipeOriginal.getServings(), recipeParsed.getServings());
        assertEquals(recipeOriginal.getImageUrl(), recipeParsed.getImageUrl());

        int ingredients = recipeOriginal.getIngredients().size();
        assertEquals(ingredients, recipeParsed.getIngredients().size());

        for (int i = 0; i < ingredients; i++) {
            assertEquals(recipeOriginal.getIngredients().get(i).getName(),
                    recipeParsed.getIngredients().get(i).getName());
            assertEquals(recipeOriginal.getIngredients().get(i).getMeasure(),
                    recipeParsed.getIngredients().get(i).getMeasure());
            assertEquals(recipeOriginal.getIngredients().get(i).getQuantity(),
                    recipeParsed.getIngredients().get(i).getQuantity());
        }

        int steps = recipeOriginal.getSteps().size();
        for (int i = 0; i < steps; i++) {
            assertEquals(recipeOriginal.getSteps().get(i).getId(),
                    recipeParsed.getSteps().get(i).getId());
            assertEquals(recipeOriginal.getSteps().get(i).getShortDescription(),
                    recipeParsed.getSteps().get(i).getShortDescription());
            assertEquals(recipeOriginal.getSteps().get(i).getDescription(),
                    recipeParsed.getSteps().get(i).getDescription());
            assertEquals(recipeOriginal.getSteps().get(i).getVideoUrl(),
                    recipeParsed.getSteps().get(i).getVideoUrl());
            assertEquals(recipeOriginal.getSteps().get(i).getThumbnailUrl(),
                    recipeParsed.getSteps().get(i).getThumbnailUrl());
        }
    }
}