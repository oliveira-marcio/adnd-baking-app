package com.android.udacity.bakingapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.udacity.bakingapp.R;
import com.android.udacity.bakingapp.data.Ingredient;
import com.android.udacity.bakingapp.data.Recipe;
import com.android.udacity.bakingapp.data.Step;
import com.android.udacity.bakingapp.utilities.JsonUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsMasterListFragment extends Fragment implements
        StepAdapter.StepAdapterOnItemClickListener {

    @BindString(R.string.recipe_shared_preferences)
    String RECIPE_SHARED_PREFERENCES;
    @BindString(R.string.recipe_current_key)
    String RECIPE_CURRENT_KEY;

    @BindView(R.id.ingredient_list)
    TextView mIngredientsListTextView;
    @BindView(R.id.servings_text_view)
    TextView mServingsTextView;
    @BindView(R.id.rv_steps)
    RecyclerView mStepsList;

    private StepAdapter mAdapter;
    private List<Step> mStepsData;

    private OnItemClickListener mCallback;

    public interface OnItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnItemClickListener");
        }
    }

    public StepsMasterListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.steps_fragment_master_list,
                container, false);
        ButterKnife.bind(this, rootView);

        SharedPreferences recipePreferences = getActivity().
                getSharedPreferences(RECIPE_SHARED_PREFERENCES, 0);
        String currentRecipe = recipePreferences.getString(RECIPE_CURRENT_KEY, null);

        Recipe recipe = JsonUtils.JsonToRecipe(currentRecipe);

        mServingsTextView.setText(
                String.format(getString(R.string.label_servings), recipe.getServings()));

        StringBuilder ingredientBuilder = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientBuilder.append(ingredient.getFullIngredient());
        }

        mIngredientsListTextView.setText(ingredientBuilder.toString());

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mStepsList.setLayoutManager(layoutManager);
        mStepsList.setHasFixedSize(true);

        mStepsData = recipe.getSteps();
        mAdapter = new StepAdapter(mStepsData, this);
        mStepsList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        mCallback.onListItemClick(clickedItemIndex);
    }
}

