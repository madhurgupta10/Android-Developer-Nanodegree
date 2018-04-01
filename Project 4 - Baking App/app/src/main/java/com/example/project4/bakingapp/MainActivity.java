package com.example.project4.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.project4.bakingapp.query.RecipeQueryTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recipe_recycler_view);

        RecipeQueryTask recipeQueryTask = (RecipeQueryTask) new RecipeQueryTask(
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json",
                this,
                recyclerView)
                .execute();
    }
}
