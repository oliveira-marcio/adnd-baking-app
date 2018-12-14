package com.android.udacity.bakingapp.ui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.udacity.bakingapp.data.Step;

import java.util.List;

public class StepDetailFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Step> mSteps;

    public StepDetailFragmentAdapter(FragmentManager fm, List<Step> steps) {
        super(fm);
        mSteps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Step step = mSteps.get(position);

        stepDetailFragment.setStepDetails(
                step.getDescription(),
                step.getVideoUrl(),
                step.getThumbnailUrl(),
                position + 1,
                getCount());

        return stepDetailFragment;
    }

    @Override
    public int getCount() {
        return (mSteps == null) ? 0 : mSteps.size();
    }
}
