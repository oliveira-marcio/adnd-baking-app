package com.android.udacity.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.udacity.bakingapp.R;
import com.android.udacity.bakingapp.data.Ingredient;
import com.android.udacity.bakingapp.data.Recipe;
import com.android.udacity.bakingapp.utilities.JsonUtils;

import java.util.List;


public class RecipeWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetRemoteViewsFactory(this.getApplicationContext());
    }
}

class RecipeWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<Ingredient> mIngredients;

    public RecipeWidgetRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mIngredients = null;

        final String RECIPE_SHARED_PREFERENCES = mContext.getString(R.string.recipe_shared_preferences);
        final String RECIPE_CURRENT_KEY = mContext.getString(R.string.recipe_current_key);

        SharedPreferences recipePreferences = mContext.getSharedPreferences(RECIPE_SHARED_PREFERENCES, 0);
        String currentRecipe = recipePreferences.getString(RECIPE_CURRENT_KEY, null);

        if (currentRecipe == null) return;

        Recipe recipe = JsonUtils.JsonToRecipe(currentRecipe);
        mIngredients = recipe.getIngredients();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (mIngredients == null || mIngredients.size() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_list_item);

        views.setTextViewText(R.id.ingredient_text_view, mIngredients.get(position).getFullIngredient());

        final Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.id.list_item, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
