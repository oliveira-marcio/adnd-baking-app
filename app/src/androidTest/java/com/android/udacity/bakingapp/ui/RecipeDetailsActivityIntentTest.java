package com.android.udacity.bakingapp.ui;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.android.udacity.bakingapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityIntentTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickRecycleViewItem_validIntentExtra() {

        final String RECIPE = "Nutella Pie";

        ViewInteraction recipesRecyclerView = onView(
                allOf(withId(R.id.rv_recipes), isDisplayed()));

        recipesRecyclerView.perform(actionOnItem(hasDescendant(withText(RECIPE)), click()));

        final int EXPECTED_CLICKED_STEP = 0;
        final String EXPECTED_STEP_NAME = "Recipe Introduction";

        ViewInteraction stepsRecyclerView = onView(
                allOf(withId(R.id.rv_steps), isDisplayed()));

        stepsRecyclerView.perform(actionOnItem(hasDescendant(withText(EXPECTED_STEP_NAME)), click()));

        intended(allOf(
                hasExtra(Intent.EXTRA_TEXT, EXPECTED_CLICKED_STEP),
                hasComponent("com.android.udacity.bakingapp.ui.StepDetailActivity")));
    }
}
