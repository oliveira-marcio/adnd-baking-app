package com.android.udacity.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.udacity.bakingapp.R;
import com.android.udacity.bakingapp.data.Recipe;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    final private RecipeAdapterOnItemClickListener mOnClickListener;

    private List<Recipe> mRecipes;
    private Context mContext;

    public interface RecipeAdapterOnItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecipeAdapter(List<Recipe> recipes, RecipeAdapterOnItemClickListener listener) {
        mRecipes = recipes;
        mOnClickListener = listener;
    }

    // Métodos clear() e addAll() criados para simular os equivalentes de um Adapter de ListView
    public void clear() {
        mRecipes.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Recipe> recipes) {
        mRecipes.clear();
        mRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        String recipeName = mRecipes.get(position).getName();
        holder.recipeName.setText(recipeName);
        holder.recipeImage.setContentDescription(recipeName);
        String recipeImageUrl = mRecipes.get(position).getImageUrl();
        Glide.with(mContext)
                .load(recipeImageUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return (mRecipes == null) ? 0 : mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name_text_view)
        TextView recipeName;
        @BindView(R.id.image_view)
        ImageView recipeImage;

        public RecipeViewHolder(View itemView) {
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
