package com.example.project4.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project4.bakingapp.adapter.RecipeIngredientAdapter;
import com.example.project4.bakingapp.adapter.RecipeStepAdapter;
import com.example.project4.bakingapp.model.Recipe;

import java.util.ArrayList;

public class RecipeStepListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean twoPane;
    private ImageView imageView;
    static Recipe recipe;
    static ArrayList<Recipe> recipeArrayList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private SubMenu subMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        subMenu = menu.addSubMenu("Recipes");

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        imageView = findViewById(R.id.img);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        int clickedItemIndex = intent.getIntExtra("index", 0);
        recipeArrayList = (ArrayList<Recipe>) bundle.getSerializable("RecipeArrayListBundle");
        recipe = recipeArrayList.get(clickedItemIndex);

        setTitle(recipe.getName());

        for (int i = 0; i < recipeArrayList.size(); i++) {
            subMenu.add(0, Menu.FIRST + i - 1, Menu.FIRST + i, recipeArrayList.get(i).getName());
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.recipestep_detail_container) != null) {
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
        RecipeIngredientAdapter myAdapter = new RecipeIngredientAdapter(recipe,
                this.getApplicationContext(), imageView);
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        //Ref https://stackoverflow.com/questions/21850817/clear-backstack-history-navigationdrawer-item-selected
        Intent intent = new Intent(this.getApplicationContext(), RecipeStepListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("RecipeArrayListBundle", recipeArrayList);
        intent.putExtra("bundle", bundle);
        intent.putExtra("index", subMenu.getItem(id).getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        this.getApplicationContext().startActivity(intent);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
