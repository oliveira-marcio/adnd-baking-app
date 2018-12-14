package com.android.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.android.udacity.bakingapp.R;
import com.android.udacity.bakingapp.data.Recipe;
import com.android.udacity.bakingapp.ui.MainActivity;
import com.android.udacity.bakingapp.ui.RecipeDetailsActivity;
import com.android.udacity.bakingapp.utilities.JsonUtils;


public class RecipeWidgetProvider extends AppWidgetProvider {

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

        final String RECIPE_SHARED_PREFERENCES = context.getString(R.string.recipe_shared_preferences);
        final String RECIPE_CURRENT_KEY = context.getString(R.string.recipe_current_key);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        SharedPreferences recipePreferences = context.getSharedPreferences(RECIPE_SHARED_PREFERENCES, 0);
        String currentRecipe = recipePreferences.getString(RECIPE_CURRENT_KEY, null);

        Intent intent;

        if (currentRecipe == null) {
            intent = new Intent(context, MainActivity.class);
            views.setTextViewText(R.id.recipe_name, context.getString(R.string.app_name));
            views.setContentDescription(R.id.recipe_image, context.getString(R.string.app_name));
        } else {
            Recipe recipe = JsonUtils.JsonToRecipe(currentRecipe);
            views.setTextViewText(R.id.recipe_name, recipe.getName());
            views.setContentDescription(R.id.recipe_image, recipe.getName());

            intent = new Intent(context, RecipeDetailsActivity.class);
        }

        views.setRemoteAdapter(R.id.widget_list, new Intent(context, RecipeWidgetRemoteViewsService.class));
        views.setEmptyView(R.id.widget_list, R.id.empty_view);

        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (MainActivity.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,
                    RecipeWidgetProvider.class));

            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }
}

