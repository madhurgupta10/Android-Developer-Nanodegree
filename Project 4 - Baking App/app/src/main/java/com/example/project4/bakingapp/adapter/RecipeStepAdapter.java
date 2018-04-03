package com.example.project4.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.project4.bakingapp.R;
import com.example.project4.bakingapp.RecipeStepDetailActivity;
import com.example.project4.bakingapp.RecipeStepDetailFragment;
import com.example.project4.bakingapp.RecipeStepListActivity;
import com.example.project4.bakingapp.model.Ingredient;
import com.example.project4.bakingapp.model.Recipe;
import com.example.project4.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.Calendar;

public class RecipeStepAdapter
        extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private final RecipeStepListActivity parentActivity;
    private final ArrayList<Step> steps;
    private final Recipe recipe;
    private final boolean twoPane;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step items = (Step) view.getTag();

            if (twoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(RecipeStepDetailFragment.ARG_ITEM_ID, items.getShortDescription());
                RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                fragment.setArguments(arguments);
                parentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipestep_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Bundle", recipe);
                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            }
        }
    };

    public RecipeStepAdapter(RecipeStepListActivity parentActivity,
                             Recipe recipe, boolean twoPane) {
        
        this.recipe = recipe;
        this.steps = (ArrayList<Step>) recipe.getSteps();
        this.parentActivity = parentActivity;
        this.twoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipestep_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.idView.setText(steps.get(position).getShortDescription());
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

        ViewHolder(View view) {
            super(view);
            idView = view.findViewById(R.id.id_text);
        }
    }
}
