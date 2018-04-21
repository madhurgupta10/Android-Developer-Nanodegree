package com.example.project4.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.project4.bakingapp.model.Recipe;
import com.example.project4.bakingapp.widget.AppWidget;

import java.util.ArrayList;

import io.paperdb.Paper;

public class RecipeStepDetailActivity extends AppCompatActivity {

    ArrayList<Recipe> recipeArrayList;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_detail);

        final int ORIENTATION = this.getApplication().getResources().getConfiguration().orientation;
        if (ORIENTATION == 1) {

            Toolbar toolbar = findViewById(R.id.detail_toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        recipeArrayList = (ArrayList<Recipe>) bundle.getSerializable("RecipeArrayListBundle");
        recipe = (Recipe) bundle.getSerializable("recipeBundle");

        bundle.putString(RecipeStepDetailFragment.ARG_ITEM_ID,
                getIntent().getStringExtra(RecipeStepDetailFragment.ARG_ITEM_ID));

        if (savedInstanceState == null) {
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipestep_detail_container, recipeStepDetailFragment)
                    .commit();
        }

        if (ORIENTATION == 1) {
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Paper.init(RecipeStepDetailActivity.this);
                    Paper.book().write("Ingredients", recipe.getIngredients());
                    Toast.makeText(RecipeStepDetailActivity.this, R.string.widget_toast,
                            Toast.LENGTH_SHORT).show();

                    Intent widgetIntent = new Intent(RecipeStepDetailActivity.this, AppWidget.class);
                    widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int[] ids = AppWidgetManager.getInstance(getApplication())
                            .getAppWidgetIds((new ComponentName(getApplication(), AppWidget.class)));
                    widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    sendBroadcast(widgetIntent);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            int clickedIndex = 0;
            for (int i = 0; i < recipeArrayList.size(); i++) {
                int recipeId = recipeArrayList.get(i).getId();
                if(recipe.getId() == recipeId){
                    clickedIndex = i;
                    break;
                }
            }
            Intent intent = new Intent(this.getApplicationContext(), RecipeStepListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("RecipeArrayListBundle", recipeArrayList);
            intent.putExtra("bundle", bundle);
            intent.putExtra("index", clickedIndex);

            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}