package com.android.udacity.bakingapp.utilities;

import com.android.udacity.bakingapp.data.Recipe;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private JsonUtils() {
    }

    /*
     * Método para fazer parsing na string JSON retornada pelo servidor e popular uma lista de
     * objetos @Recipe
     */
    public static List<Recipe> getRecipesFromJson(String recipesJsonStr)
            throws JSONException {

        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE_URL = "image";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String RECIPE_STEPS = "steps";

        final String INGREDIENT_QUANTITY = "quantity";
        final String INGREDIENT_MEASURE = "measure";
        final String INGREDIENT_NAME = "ingredient";

        final String STEP_ID = "id";
        final String STEP_SHORT_DESCRIPTION = "shortDescription";
        final String STEP_DESCRIPTION = "description";
        final String STEP_VIDEO_URL = "videoURL";
        final String STEP_THUMBNAIL_URL = "thumbnailURL";

        List<Recipe> recipes = new ArrayList<>();

        JSONArray recipeJsonArray = new JSONArray(recipesJsonStr);

        for (int i = 0; i < recipeJsonArray.length(); i++) {
            JSONObject currentRecipe = recipeJsonArray.getJSONObject(i);

            long recipeId = currentRecipe.getLong(RECIPE_ID);
            String recipeName = currentRecipe.getString(RECIPE_NAME);
            int recipeServings = currentRecipe.getInt(RECIPE_SERVINGS);
            String recipeImageUrl = currentRecipe.optString(RECIPE_IMAGE_URL);

            Recipe recipe = new Recipe(recipeId, recipeName, recipeServings, recipeImageUrl);

            JSONArray recipeIngredients = currentRecipe.getJSONArray(RECIPE_INGREDIENTS);
            for (int j = 0; j < recipeIngredients.length(); j++) {
                JSONObject currentIngredient = recipeIngredients.getJSONObject(j);
                double ingredientQuantity = currentIngredient.getDouble(INGREDIENT_QUANTITY);
                String ingredientMeasure = currentIngredient.getString(INGREDIENT_MEASURE);
                String ingredientName = currentIngredient.getString(INGREDIENT_NAME);

                recipe.addIngredient(ingredientQuantity, ingredientMeasure, ingredientName);
            }

            JSONArray recipeSteps = currentRecipe.getJSONArray(RECIPE_STEPS);
            for (int j = 0; j < recipeSteps.length(); j++) {
                JSONObject currentStep = recipeSteps.getJSONObject(j);
                long stepId = currentStep.getLong(STEP_ID);
                String stepShortDescription = currentStep.getString(STEP_SHORT_DESCRIPTION);
                String stepDescription = currentStep.getString(STEP_DESCRIPTION);
                String stepVideoUrl = currentStep.optString(STEP_VIDEO_URL);
                String stepThumbnailUrl = currentStep.optString(STEP_THUMBNAIL_URL);

                recipe.addStep(stepId, stepShortDescription, stepDescription, stepVideoUrl,
                        stepThumbnailUrl);
            }

            recipes.add(recipe);
        }

        return recipes;
    }

    /*
     * Método para serializar um objeto @Recipe numa String JSON.
     */
    public static String RecipeToJson(Recipe recipe) {
        Gson gson = new Gson();
        return gson.toJson(recipe);
    }

    /*
     * Método para recuperar um objeto @Recipe serializado em uma String JSON.
     */
    public static Recipe JsonToRecipe(String recipeJson) {
        Gson gson = new Gson();
        return gson.fromJson(recipeJson, Recipe.class);
    }
}