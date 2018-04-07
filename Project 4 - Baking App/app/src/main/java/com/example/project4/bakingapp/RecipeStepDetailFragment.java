package com.example.project4.bakingapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project4.bakingapp.model.Recipe;
import com.example.project4.bakingapp.model.Step;
import com.squareup.picasso.Picasso;

import static com.example.project4.bakingapp.RecipeStepListActivity.recipe;

public class RecipeStepDetailFragment extends Fragment {
    private static Step step;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle args = getArguments();
            step = (Step) args.getSerializable("stepBundle");
            recipe = (Recipe) args.getSerializable("recipeBundle");

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            ImageView imageView = activity.findViewById(R.id.img_toolbar);

            Picasso.with(this.getContext())
                    .load(recipe.getImage())
                    .fit()
                    .centerCrop()
                    .into(imageView);

            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipestep_detail, container, false);

        if (step != null) {
            ((TextView) rootView.findViewById(R.id.recipestep_detail)).setText(step.getShortDescription());
        }

        return rootView;
    }
}