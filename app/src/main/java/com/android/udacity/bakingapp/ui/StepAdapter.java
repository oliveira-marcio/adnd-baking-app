package com.android.udacity.bakingapp.ui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.udacity.bakingapp.R;
import com.android.udacity.bakingapp.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    final private StepAdapterOnItemClickListener mOnClickListener;

    private List<Step> mSteps;
    private Context mContext;

    public interface StepAdapterOnItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public StepAdapter(List<Step> steps, StepAdapterOnItemClickListener listener) {
        mSteps = steps;
        mOnClickListener = listener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        String stepShortDescription = mSteps.get(position).getShortDescription();
        holder.numberStep.setText("" + (position + 1));
        holder.numberStep.setContentDescription(
                String.format(mContext.getString(R.string.step_number_description),
                        (position + 1)));
        holder.stepShortDescription.setText(stepShortDescription);
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.number_step)
        TextView numberStep;
        @BindView(R.id.name_text_view)
        TextView stepShortDescription;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
