package com.android.udacity.bakingapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.udacity.bakingapp.R;
import com.android.udacity.bakingapp.data.Recipe;
import com.android.udacity.bakingapp.data.Step;
import com.android.udacity.bakingapp.utilities.JsonUtils;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements
        StepsMasterListFragment.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindString(R.string.recipe_shared_preferences)
    String RECIPE_SHARED_PREFERENCES;
    @BindString(R.string.recipe_current_key)
    String RECIPE_CURRENT_KEY;

    @BindBool(R.bool.use_grid_layout)
    boolean mTwoPane;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        SharedPreferences recipePreferences = getSharedPreferences(RECIPE_SHARED_PREFERENCES, 0);
        String currentRecipe = recipePreferences.getString(RECIPE_CURRENT_KEY, null);

        mRecipe = JsonUtils.JsonToRecipe(currentRecipe);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mRecipe.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mTwoPane) {
            if (savedInstanceState == null) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();

                Step step = mRecipe.getSteps().get(0);

                stepDetailFragment.setStepDetails(
                        step.getDescription(),
                        step.getVideoUrl(),
                        step.getThumbnailUrl(),
                        1,
                        mRecipe.getSteps().size());

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mTwoPane) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();

            Step step = mRecipe.getSteps().get(clickedItemIndex);

            stepDetailFragment.setStepDetails(
                    step.getDescription(),
                    step.getVideoUrl(),
                    step.getThumbnailUrl(),
                    clickedItemIndex + 1,
                    mRecipe.getSteps().size());

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, clickedItemIndex);
            startActivity(intent);
        }
    }
}
