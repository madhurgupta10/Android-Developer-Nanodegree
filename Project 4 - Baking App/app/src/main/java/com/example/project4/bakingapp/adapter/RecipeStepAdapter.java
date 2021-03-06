package com.example.project4.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project4.bakingapp.R;
import com.example.project4.bakingapp.RecipeStepDetailActivity;
import com.example.project4.bakingapp.RecipeStepDetailFragment;
import com.example.project4.bakingapp.RecipeStepListActivity;
import com.example.project4.bakingapp.model.Recipe;
import com.example.project4.bakingapp.model.Step;

import java.util.ArrayList;

public class RecipeStepAdapter
        extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private final RecipeStepListActivity parentActivity;
    private final ArrayList<Step> steps;
    private final Recipe recipe;
    private final boolean twoPane;
    private final ArrayList<Recipe> recipeArrayList;

    public RecipeStepAdapter(RecipeStepListActivity parentActivity,
                             Recipe recipe, boolean twoPane, ArrayList<Recipe> recipeArrayList) {
        
        this.recipe = recipe;
        this.steps = (ArrayList<Step>) recipe.getSteps();
        this.parentActivity = parentActivity;
        this.twoPane = twoPane;
        this.recipeArrayList = recipeArrayList;
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step step = (Step) view.getTag();

            if (twoPane) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("stepBundle", step);
                bundle.putSerializable("recipeBundle", recipe);
                bundle.putSerializable("RecipeArrayListBundle", recipeArrayList);
                bundle.putString(RecipeStepDetailFragment.ARG_ITEM_ID, String.valueOf(step.getId()));
                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                recipeStepDetailFragment.setArguments(bundle);
                parentActivity.getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipestep_detail_container, recipeStepDetailFragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("stepBundle", step);
                bundle.putSerializable("recipeBundle", recipe);
                bundle.putSerializable("RecipeArrayListBundle", recipeArrayList);
                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            }
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipestep_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.idView.setText(steps.get(position).getShortDescription());

        Glide.with(this.parentActivity.getApplicationContext())
                .load(steps.get(position).getVideoURL())
                .into(holder.imageView);

        holder.itemView.setTag(steps.get(position));
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView idView;
        final ImageView imageView;

        ViewHolder(View view) {
            super(view);
            idView = view.findViewById(R.id.id_text);
            imageView = view.findViewById(R.id.step_card_iv);
        }
    }
}
