package com.example.project4.bakingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.project4.bakingapp.adapter.RecipeAdapter;
import com.example.project4.bakingapp.model.Recipe;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    private boolean isTablet;
    private String queryUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final int LOADER_ID = 22;
    private String data;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recipe_recycler_view);

        isTablet = getResources().getBoolean(R.bool.is_tablet);

        if (savedInstanceState == null) {

            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryUrl", queryUrl);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Object> loader = loaderManager.getLoader(LOADER_ID);

            if (loader == null) {
                loaderManager.initLoader(LOADER_ID, queryBundle,
                        this);
            } else {
                loaderManager.restartLoader(LOADER_ID, queryBundle, this);
            }
        } else {
            String data = savedInstanceState.getString("data");
            this.data = data;
            try {
                setRecyclerView(data, recyclerView, isTablet);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle ARGS) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (ARGS == null) {
                    return;
                }
                forceLoad();
            }
            @Override
            public String loadInBackground() {
                queryUrl = ARGS.getString("queryUrl");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(queryUrl)
                        .build();

                String data = null;

                try {
                    Response response = client.newCall(request).execute();
                    assert response.body() != null;
                    data = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        this.data = data;
        if (data != null) {
            try {
                setRecyclerView(data, recyclerView, isTablet);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("data", data);
    }

    private void setRecyclerView(String data, RecyclerView recyclerView, boolean isTablet)
            throws JSONException {

        final ArrayList<Recipe> recipeArrayList = new ArrayList<Recipe>();
        JSONArray recipes = new JSONArray(data);
        for (int i = 0; i < recipes.length(); i++) {
            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(String.valueOf(recipes.optJSONObject(i)),
                    Recipe.class);

            recipeArrayList.add(recipe);
        }

        if (isTablet) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        RecipeAdapter.listItemClickListener onClickListener = new RecipeAdapter.listItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                Intent intent = new Intent(MainActivity.this, RecipeStepListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("RecipeArrayListBundle", recipeArrayList);
                intent.putExtra("bundle", bundle);
                intent.putExtra("index", clickedItemIndex);
                MainActivity.this.startActivity(intent);
            }
        };

        RecipeAdapter recipeAdapter = new RecipeAdapter(recipes.length(), recipeArrayList,
                onClickListener, this);
        recyclerView.setAdapter(recipeAdapter);
    }
}
