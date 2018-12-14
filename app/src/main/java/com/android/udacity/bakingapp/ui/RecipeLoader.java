package com.android.udacity.bakingapp.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.android.udacity.bakingapp.IdlingResource.SimpleIdlingResource;
import com.android.udacity.bakingapp.data.Recipe;
import com.android.udacity.bakingapp.utilities.JsonUtils;
import com.android.udacity.bakingapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;


public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private URL mUrl;
    private List<Recipe> mData;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    public RecipeLoader(Context context, URL url,
                        @Nullable final SimpleIdlingResource idlingResource) {
        super(context);
        mUrl = url;
        mIdlingResource = idlingResource;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        mData = data;
        super.deliverResult(data);
    }

    @Override
    public List<Recipe> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        List<Recipe> recipes = null;
        String searchResults;

        try {
            searchResults = NetworkUtils.getResponseFromHttpUrl(mUrl);
            recipes = JsonUtils.getRecipesFromJson(searchResults);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }

        return recipes;
    }
}
