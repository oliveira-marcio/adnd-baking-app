package com.android.udacity.bakingapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.android.udacity.bakingapp.R;
import com.android.udacity.bakingapp.data.Recipe;
import com.android.udacity.bakingapp.utilities.JsonUtils;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {

    @BindString(R.string.recipe_shared_preferences)
    String RECIPE_SHARED_PREFERENCES;
    @BindString(R.string.recipe_current_key)
    String RECIPE_CURRENT_KEY;
    @BindBool(R.bool.use_grid_layout)
    boolean mIsTablet;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.root_view)
    FrameLayout rootView;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI();
        }

        SharedPreferences recipePreferences = getSharedPreferences(RECIPE_SHARED_PREFERENCES, 0);
        String currentRecipe = recipePreferences.getString(RECIPE_CURRENT_KEY, null);

        if (currentRecipe == null) finish();

        mRecipe = JsonUtils.JsonToRecipe(currentRecipe);
        setTitle(mRecipe.getName());

        StepDetailFragmentAdapter adapter = new StepDetailFragmentAdapter(
                getSupportFragmentManager(), mRecipe.getSteps());

        int stepIndex = getIntent().getIntExtra(Intent.EXTRA_TEXT, 0);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(stepIndex);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                unfixImmersivePadding();
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            showSystemUI();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fixImmersivePadding();
            }
        }
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    // Os métodos abaixo corrigem o overlap da Status Bar quando o aplicativo sai do modo imersivo
    // (tela completamente cheia, sem Action Bar, Status Bar e Navigation Bar)
    //
    // Apesar da documentação oficial no site Android Developer sugerir os métodos como implementados
    // acima (hideSystemUI e showSystemUI) para entrar e sair do modo imersivo, ainda ocorre um
    // overlap da StatusBar sobre as demais views da Activity quando o aplicativo retorna do modo
    // imersivo, então a solução que encontrei foi setar manualmente um paddingTop na View raiz da
    // Activity equivalente à altura da Status Bar
    //
    // Para reentrar em modo imersivo é necessário remover esse padding extra.
    private void fixImmersivePadding() {
        int statusBarResId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarResId > 0) {
            int StatusBarHeight = getResources().getDimensionPixelSize(statusBarResId);
            rootView.setPadding(0, StatusBarHeight, 0, 0);
        }
    }

    private void unfixImmersivePadding() {
        rootView.setPadding(0, 0, 0, 0);
    }
}
