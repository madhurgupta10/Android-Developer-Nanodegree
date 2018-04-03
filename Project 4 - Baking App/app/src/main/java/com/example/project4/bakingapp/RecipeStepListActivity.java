package com.example.project4.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.example.project4.bakingapp.adapter.RecipeIngredientAdapter;
import com.example.project4.bakingapp.adapter.RecipeStepAdapter;
import com.example.project4.bakingapp.model.Ingredient;
import com.example.project4.bakingapp.model.Recipe;
import com.example.project4.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * An activity representing a list of recipe_steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean twoPane;
    static Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_list);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        recipe = (Recipe) bundle.getSerializable("Bundle");

        setTitle(recipe.getName());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.recipestep_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true;
        }

        View recyclerView = findViewById(R.id.recipestep_list);
        View ingRecyclerView = findViewById(R.id.ing_rv);

        setupRecyclerView((RecyclerView) recyclerView);
        setupIngRecyclerView((RecyclerView) ingRecyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecipeStepAdapter(this, recipe, twoPane));
    }
    private void setupIngRecyclerView(@NonNull RecyclerView recyclerView) {

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecipeIngredientAdapter myAdapter = new RecipeIngredientAdapter(
                (ArrayList<Ingredient>) recipe.getIngredients(), this.getApplicationContext());
        recyclerView.setAdapter(myAdapter);

    }
}
