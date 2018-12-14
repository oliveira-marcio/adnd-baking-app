package com.android.udacity.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.udacity.bakingapp.IdlingResource.SimpleIdlingResource;
import com.android.udacity.bakingapp.R;
import com.android.udacity.bakingapp.data.Recipe;
import com.android.udacity.bakingapp.utilities.JsonUtils;
import com.android.udacity.bakingapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Recipe>>,
        RecipeAdapter.RecipeAdapterOnItemClickListener {

    private static final int RECIPE_LOADER_ID = 1;
    public static final String ACTION_DATA_UPDATED =
            "com.android.udacity.bakingapp.ACTION_DATA_UPDATED";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_recipes)
    RecyclerView mRecipesList;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyStateView;
    @BindView(R.id.image_empty_view)
    ImageView mEmptyStateImageView;
    @BindView(R.id.text_empty_view)
    TextView mEmptyStateTextView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindInt(R.integer.recipe_grid_columns)
    int mGridColumns;

    @BindString(R.string.recipe_shared_preferences)
    String RECIPE_SHARED_PREFERENCES;
    @BindString(R.string.recipe_current_key)
    String RECIPE_CURRENT_KEY;

    private RecipeAdapter mAdapter;
    private List<Recipe> mRecipesData = new ArrayList<>();

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIElements();

        mAdapter = new RecipeAdapter(mRecipesData, this);
        mRecipesList.setAdapter(mAdapter);

        if (hasInternetConnection()) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            showErrorMessage(R.string.no_internet_connection);
        }

        getIdlingResource();
    }

    public void initializeUIElements() {
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        GridLayoutManager layoutManager = new GridLayoutManager(this, mGridColumns);
        mRecipesList.setLayoutManager(layoutManager);
        mRecipesList.setHasFixedSize(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadRecipes();
            }
        });
    }

    public boolean hasInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showErrorMessage(int errorId) {
        mEmptyStateView.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setText(errorId);
        mEmptyStateImageView.setContentDescription(getString(errorId));
        mRecipesList.setVisibility(View.GONE);
    }

    private void showMoviesDataView() {
        mEmptyStateView.setVisibility(View.GONE);
        mRecipesList.setVisibility(View.VISIBLE);
    }

    public void reloadRecipes() {
        mAdapter.clear();
        mSwipeRefreshLayout.setRefreshing(false);
        mEmptyStateView.setVisibility(View.GONE);
        updateSharedPreferences(null, true);
        updateWidgets();

        if (hasInternetConnection()) {
            getSupportLoaderManager().restartLoader(RECIPE_LOADER_ID, null, this);
        } else {
            showErrorMessage(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        URL url = NetworkUtils.buildUrl();
        return new RecipeLoader(this, url, mIdlingResource);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
        mLoadingIndicator.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.clear();

        if (recipes != null) {
            showMoviesDataView();
            mRecipesData = recipes;
            mAdapter.addAll(mRecipesData);
        } else {
            showErrorMessage(R.string.no_recipes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mAdapter.clear();
    }

    public void updateWidgets() {
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(getPackageName());
        sendBroadcast(dataUpdatedIntent);
    }

    public void updateSharedPreferences(String recipe, boolean clearAll) {
        SharedPreferences recipePreferences = getSharedPreferences(RECIPE_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = recipePreferences.edit();
        if (clearAll) {
            editor.clear();
        } else {
            editor.putString(RECIPE_CURRENT_KEY, recipe);
        }
        editor.apply();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        // SharedPreferences utilizada no lugar do Extra do Intent porque também será acessado
        // pelo Widget para exibir os ingredientes da última receita clicada.
        String currentRecipe = JsonUtils.RecipeToJson(mRecipesData.get(clickedItemIndex));
        updateSharedPreferences(currentRecipe, false);

        updateWidgets();
        startActivity(new Intent(this, RecipeDetailsActivity.class));
    }
}
